package com.soosmart.facts.exceptions.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final AccessDeniedException exception) {
       
        return ProblemDetail.forStatusAndDetail(
                FORBIDDEN,
                "Vos droits ne vous permettent pas d'effectuer cette action"
        );
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final BadCredentialsException exception) {
       
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "identifiants invalides"
        );
        problemDetail.setProperty("erreur", "nous n'avons pas pu vous identifier");
        return problemDetail;
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = ExpiredJwtException.class)
    public @ResponseBody
    ProblemDetail expiredJwtException(final ExpiredJwtException exception) {
       
        return ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "Token expiré"
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody
    ProblemDetail exception(final Exception exception) {
       
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                NOT_FOUND,
                "Token expired or invalide"
        );
        problemDetail.setProperty("erreur", "token expiré ou invalide");
        return problemDetail;
    }
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = {MalformedJwtException.class, SignatureException.class})
    public @ResponseBody
    ProblemDetail badCredentialsException(final Exception exception) {
       
        return ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "Token invalide"
        );
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {RuntimeException.class})
    public @ResponseBody
    ProblemDetail runtimeException(final RuntimeException exception) {
       
        return ProblemDetail.forStatusAndDetail(
                INTERNAL_SERVER_ERROR,
                "une erreur est survenue"
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public @ResponseBody
    ProblemDetail entityNotFoundException(final EntityNotFoundException exception) {

        return ProblemDetail.forStatusAndDetail(
                NOT_FOUND,
                "Entity not found"
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public @ResponseBody
    ProblemDetail illegalArgumentException(final IllegalArgumentException exception) {

        return ProblemDetail.forStatusAndDetail(
                NOT_FOUND,
                "Illegal argument"
        );
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public @ResponseBody
    ProblemDetail UsernameNotFoundException(final UsernameNotFoundException exception) {

        return ProblemDetail.forStatusAndDetail(
                NOT_FOUND,
                "Username not found"
        );
    }
}
