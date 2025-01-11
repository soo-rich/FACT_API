package com.soosmart.facts.exceptions.user;

public class UsernameExiste extends RuntimeException {
    public UsernameExiste(String message) {
        super(message);
    }
}
