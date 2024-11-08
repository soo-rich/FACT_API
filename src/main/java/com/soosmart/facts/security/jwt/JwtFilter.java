package com.soosmart.facts.security.jwt;

import com.soosmart.facts.entity.jwt.Jwt;
import com.soosmart.facts.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Service
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private JwtService jwtService;
    private UtilisateurService utilisateurService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        Jwt tokenEntity = null;
        String username = null;
        Boolean isTokenExpired = true;

        try {
            final String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                tokenEntity = this.jwtService.tokenByValue(token);
                isTokenExpired = this.jwtService.isTokenExpired(token);
                username = this.jwtService.extractUsername(token);
            }

            if (
                    !isTokenExpired
                            && tokenEntity.getUtilisateur().getUsername().equals(username)
                            && SecurityContextHolder.getContext().getAuthentication() == null
            ) {
                UserDetails userDetails = this.utilisateurService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            }
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            this.handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
