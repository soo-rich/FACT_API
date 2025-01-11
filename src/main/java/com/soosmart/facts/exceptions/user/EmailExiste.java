package com.soosmart.facts.exceptions.user;

public class EmailExiste extends RuntimeException {
    public EmailExiste(String message) {
        super(message);
    }
}
