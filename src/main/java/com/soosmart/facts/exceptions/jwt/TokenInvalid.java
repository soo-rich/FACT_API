package com.soosmart.facts.exceptions.jwt;

public class TokenInvalid extends RuntimeException {
    public TokenInvalid(String message) {
        super(message);
    }
}
