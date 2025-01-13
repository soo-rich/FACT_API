package com.soosmart.facts.dto.user.authentication;

import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;
import org.springframework.http.converter.HttpMessageNotReadableException;

public record RefreshTokenDTO(
        String refresh
) {
    public RefreshTokenDTO{
        if (refresh == null|| refresh.isBlank()) {
         throw new HttpMessageNotReadableException("refresh token is blank or null");
        }
    }
}
