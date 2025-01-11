package com.soosmart.facts.dto.user.authentication;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;

public record AuthenticationDTO(
        String username,
        String password
) {
    public AuthenticationDTO {
        if (username == null || username.isBlank()) {
            throw new DtoArgumentRquired("Username cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new DtoArgumentRquired("Password cannot be null or empty");
        }
    }

}
