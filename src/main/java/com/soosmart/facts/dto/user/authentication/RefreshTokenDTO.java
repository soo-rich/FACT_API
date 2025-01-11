package com.soosmart.facts.dto.user.authentication;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;

public record RefreshTokenDTO(
        String refresh
) {
    public RefreshTokenDTO{
        if (refresh == null|| refresh.isBlank()) {
         throw new DtoArgumentRquired("refresh token is blank or null");
        }
    }
}
