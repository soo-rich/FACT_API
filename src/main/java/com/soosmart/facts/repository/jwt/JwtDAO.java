package com.soosmart.facts.repository.jwt;

import com.soosmart.facts.entity.jwt.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface JwtDAO extends JpaRepository<Jwt, UUID> {

    Optional<Jwt> findByValeur(String token);
    Optional<Jwt> findByValeurAndDesactiveAndExpire(String token, boolean desactive, boolean expire);

    /**
     * Find a valid token for a user
     * @param utilisateur_username the username of the user
     * @param desactive the desactive status of the token
     * @param expire the expire status of the token
     * @return the token
     */
    Optional<Jwt> findByUtilisateur_UsernameAndDesactiveAndExpire(String utilisateur_username, boolean desactive, boolean expire);

    /**
     * Find all tokens for a user
     * @param utilisateur_username the username of the user
     * */
    Stream<Jwt> findByUtilisateurUsername(String utilisateur_username);

    /**
     * Find a token by its refresh token value
     * @param value the value of the refresh token
     * @return the token
     */
    Optional<Jwt> findByRefreshTokenValue(String value);

    /**
     * Delete all tokens by their expire and desactive status
     * @param expire the expire status
     * @param desactive the desactive status
     */
    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);
}
