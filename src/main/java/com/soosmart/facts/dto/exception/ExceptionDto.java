package com.soosmart.facts.dto.exception;

public record ExceptionDto(
        org.springframework.http.HttpStatus code,
        String message
) {
}
