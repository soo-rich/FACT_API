package com.soosmart.facts.exceptions;

public class TokenExpireException extends RuntimeException{
    public TokenExpireException (String msg){
        super(msg);
    }
}
