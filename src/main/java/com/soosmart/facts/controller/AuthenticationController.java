package com.soosmart.facts.controller;

import com.soosmart.facts.dto.user.authentication.AuthenticationDTO;
import com.soosmart.facts.dto.user.authentication.RefreshTokenDTO;
import com.soosmart.facts.security.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> connexion(@RequestBody AuthenticationDTO authenticationDTO) {
        final org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.username(), authenticationDTO.password())
        );

        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(200).body(this.jwtService.generateToken(authenticationDTO.username()));
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenDTO refreshTokenRequest) {
        return ResponseEntity.status(200).body(this.jwtService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout() {
        this.jwtService.deconnexion();
        return ResponseEntity.status(200).build();
    }


}
