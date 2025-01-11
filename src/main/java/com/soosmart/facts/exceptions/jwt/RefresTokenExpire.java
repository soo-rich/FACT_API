package com.soosmart.facts.exceptions.jwt;

public class RefresTokenExpire extends RuntimeException {
    public RefresTokenExpire(String message) {
        super(message);
    }
}
