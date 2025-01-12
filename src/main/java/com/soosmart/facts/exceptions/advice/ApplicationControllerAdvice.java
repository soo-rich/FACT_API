package com.soosmart.facts.exceptions.advice;

import com.soosmart.facts.dto.exception.ExceptionDto;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.exceptions.NotSameId;
import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;
import com.soosmart.facts.exceptions.jwt.RefresTokenExpire;
import com.soosmart.facts.exceptions.jwt.RefreshTokenInvalid;
import com.soosmart.facts.exceptions.jwt.TokenExpireException;
import com.soosmart.facts.exceptions.jwt.TokenInvalid;
import com.soosmart.facts.exceptions.user.EmailExiste;
import com.soosmart.facts.exceptions.user.UsernameExiste;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@ControllerAdvice
public class ApplicationControllerAdvice {

   /* @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public @ResponseBody ExceptionDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ExceptionDto(BAD_REQUEST, ex.getMessage());
    }*/

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = {RefreshTokenInvalid.class})
    public @ResponseBody ExceptionDto refreshtokeninvalid(RefreshTokenInvalid ex) {
        return new ExceptionDto(FORBIDDEN, ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = {RefresTokenExpire.class})
    public @ResponseBody ExceptionDto refreshtokeninvalid(RefresTokenExpire ex) {
        return new ExceptionDto(FORBIDDEN, ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = TokenExpireException.class)
    public @ResponseBody
    ExceptionDto badCredentialsException(TokenExpireException exception) {

        return new ExceptionDto(FORBIDDEN, exception.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = TokenInvalid.class)
    public @ResponseBody
    ExceptionDto badCredentialsException(TokenInvalid exception) {

        return new ExceptionDto(FORBIDDEN, exception.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody
    ExceptionDto badCredentialsException(AccessDeniedException exception) {

        return new ExceptionDto(UNAUTHORIZED, "Accès refusé");
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody
    ExceptionDto badCredentialsException(final BadCredentialsException exception) {
        return new ExceptionDto(UNAUTHORIZED, "Mauvais identifiants");
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(value = ExpiredJwtException.class)
    public @ResponseBody
    ExceptionDto expiredJwtException(final ExpiredJwtException exception) {
        return new ExceptionDto(
                NOT_ACCEPTABLE,
                exception.getMessage()
        );
    }


    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(value = MalformedJwtException.class)
    public @ResponseBody
    ExceptionDto malformedJwtException(final MalformedJwtException exception) {

        return new ExceptionDto(
                NOT_ACCEPTABLE,
                "Token mal formé"
        );
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(value = SignatureException.class)
    public @ResponseBody
    ExceptionDto signatureException(final SignatureException exception) {

        return new ExceptionDto(
                NOT_ACCEPTABLE,
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
    @ExceptionHandler(value = {EntityNotFound.class})
    public @ResponseBody
    ExceptionDto entityNotFoundException(final EntityNotFound exception) {
        return new ExceptionDto(
                NOT_FOUND,
                exception.getMessage()
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public @ResponseBody
    ExceptionDto illegalArgumentException(final IllegalArgumentException exception) {

        return new ExceptionDto(
                BAD_REQUEST,
                exception.getMessage()
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {DtoArgumentRquired.class})
    public @ResponseBody
    ExceptionDto dtorequire(final DtoArgumentRquired exception) {

        return new ExceptionDto(
                BAD_REQUEST,
                exception.getMessage()
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

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = {EmailExiste.class, UsernameExiste.class})
    public @ResponseBody
    ExceptionDto emailExiste(final EmailExiste exception, final UsernameExiste exception2) {

        return new ExceptionDto(
                CONFLICT,
                exception.getMessage() + "  " + exception2.getMessage()
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = {NotSameId.class})
    public @ResponseBody
    ExceptionDto notSameId(final NotSameId exception) {

        return new ExceptionDto(
                BAD_REQUEST,
                exception.getMessage()
        );
    }

}
