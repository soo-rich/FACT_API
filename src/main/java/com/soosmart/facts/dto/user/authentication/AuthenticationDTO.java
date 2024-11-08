package com.soosmart.facts.dto.user.authentication;

public record AuthenticationDTO(
        String username,
        String password
) {
    public AuthenticationDTO {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

}
