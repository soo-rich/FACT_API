package com.soosmart.facts.security.jwt;

import com.soosmart.facts.dto.user.authentication.RefreshTokenDTO;
import com.soosmart.facts.entity.jwt.Jwt;
import com.soosmart.facts.entity.jwt.RefreshToken;
import com.soosmart.facts.entity.user.Utilisateur;
import com.soosmart.facts.repository.jwt.JwtDAO;
import com.soosmart.facts.security.user.UtilisateurConnecteServie;
import com.soosmart.facts.service.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;



@Service
@Transactional
public class JwtService {

    @Value("${my.secretkey}")
    private  String ENCRYPTION_KEY;
    private static final String TOKEN_INVALIDE = "Token invalide";
    private static final String REFRESH_TOKEN_INVALIDE = "Refresh token invalide";
    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    private final UtilisateurService utilisateurService;
    private final JwtDAO jwtDAO;
    private final UtilisateurConnecteServie utilisateurConnecteServie;

    public JwtService(UtilisateurService utilisateurService, JwtDAO jwtDAO, UtilisateurConnecteServie utilisateurConnecteServie) {
        this.utilisateurService = utilisateurService;
        this.jwtDAO = jwtDAO;
        this.utilisateurConnecteServie = utilisateurConnecteServie;
    }


    public Jwt tokenByValue(String token) {
        return this.jwtDAO.findByValeurAndDesactiveAndExpire(token, false, false)
                .orElseThrow(() -> new RuntimeException("Token invalide"));
    }

    /**
     * @param utilisateur on desactive tous les tokens de l'utilisateur
     */

    public void disableToken(Utilisateur utilisateur) {
        final List<Jwt> jwtList = this.jwtDAO.findByUtilisateurUsername(utilisateur.getUsername()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).toList();
        this.jwtDAO.saveAll(jwtList);
    }


    public Map<String, String> generateToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        this.disableToken(utilisateur);
        Map<String, String> jwtmap = new HashMap<>(this.generateJwtToken(utilisateur));

        RefreshToken refreshToken = RefreshToken.builder()
                .value(UUID.randomUUID().toString())
                .expired(false)
                .expirationDate(Instant.now().minusMillis( 60 * 60 * 1000 * 2))// 2 heures
                .build();

        Jwt jwt = Jwt.builder()
                .valeur(jwtmap.get(BEARER))
                .expire(false)
                .desactive(false)
                .utilisateur(utilisateur)
                .refreshToken(refreshToken)
                .build();

        this.jwtDAO.save(jwt);
        jwtmap.put(REFRESH, refreshToken.getValue());
        return jwtmap;
    }

    private Map<String, String> generateJwtToken(Utilisateur utilisateur) {

        final long currentTime = System.currentTimeMillis(); // temps actuel
        final long expirationTime = currentTime + 60 * 60 * 1000; // 1 heure
        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom() != null ? utilisateur.getNom():"",
                "prenom", utilisateur.getPrenom()!= null ? utilisateur.getPrenom():"",
                "email", utilisateur.getEmail()!= null ? utilisateur.getEmail():"",
                "numero", utilisateur.getNumero()!= null ? utilisateur.getNumero():"",
                "role", utilisateur.getRole().getLibelle(),
                Claims.EXPIRATION, new Date(expirationTime),// date d'expiration
                Claims.SUBJECT, utilisateur.getUsername(),// sujet
                Claims.ISSUER, "soosmart"// emetteur
        );

        final String bearer = Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(utilisateur.getUsername())
                .claims(claims)
                .signWith(getKey())
                .compact();

        return Map.of(BEARER, bearer);
    }

    private Key getKey() {

        final byte[] decode = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decode);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(
                        this.getKey()
                ).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void deconnexion() {
        this.disableToken(this.utilisateurConnecteServie.getUtilisateurConnecte());
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public Map<String, String> refreshToken(RefreshTokenDTO refreshTokenRequest){
        final String refreshToken = refreshTokenRequest.refresh();
        final Jwt jwt = this.jwtDAO.findByRefreshTokenValue(refreshToken)
                .orElseThrow(() -> new RuntimeException(REFRESH_TOKEN_INVALIDE));

        if (jwt.getRefreshToken().getExpired()||jwt.getRefreshToken().getExpirationDate().isBefore(Instant.now())){
            throw new RuntimeException("Refresh token expiré");
        }
        this.disableToken(jwt.getUtilisateur());
        return this.generateToken(jwt.getUtilisateur().getUsername());
    }

    @Scheduled(cron = "@daily")
    public void removeUselessJwt(){
        this.jwtDAO.deleteAllByExpireAndDesactive(true,true);
    }
}
