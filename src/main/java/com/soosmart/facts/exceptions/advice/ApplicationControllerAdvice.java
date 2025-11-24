package com.soosmart.facts.exceptions.advice;


import com.soosmart.facts.dto.exception.ExceptionDto;
import com.soosmart.facts.exceptions.EntityNotFound;
import com.soosmart.facts.exceptions.NotSameId;
import com.soosmart.facts.exceptions.dto.DtoArgumentRquired;
import com.soosmart.facts.exceptions.file.FileNotFoundException;
import com.soosmart.facts.exceptions.file.FileStorageException;
import com.soosmart.facts.exceptions.file.FileValidationException;
import com.soosmart.facts.exceptions.jwt.RefresTokenExpire;
import com.soosmart.facts.exceptions.jwt.RefreshTokenInvalid;
import com.soosmart.facts.exceptions.jwt.TokenExpireException;
import com.soosmart.facts.exceptions.jwt.TokenInvalid;
import com.soosmart.facts.exceptions.user.EmailExiste;
import com.soosmart.facts.exceptions.user.UsernameExiste;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.http.HttpStatus.*;

@RestController
@ControllerAdvice
public class ApplicationControllerAdvice {

   /* @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public @ResponseBody ExceptionDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ExceptionDto(BAD_REQUEST, ex.getMessage());
    }*/
   private static final Logger logger = LoggerFactory.getLogger(ApplicationControllerAdvice.class);
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

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(value = EntityExistsException.class)
    public @ResponseBody ExceptionDto entityExistsException(final EntityExistsException exception) {
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

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public @ResponseBody
    ExceptionDto httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        int index = exception.getMessage().indexOf("problem: ");
        if (index != -1) {
            return new ExceptionDto(
                    BAD_REQUEST,
                    exception.getMessage().substring(index + "problem: ".length())
            );
        } else {
            return new ExceptionDto(
                    BAD_REQUEST,
                    "Erreur de lecture"
            );
        }

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
    @ExceptionHandler(value = UsernameExiste.class)
    public @ResponseBody
    ExceptionDto usernameExiste(final UsernameExiste exception) {

        return new ExceptionDto(
                CONFLICT,
                exception.getMessage()
        );
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = EmailExiste.class)
    public @ResponseBody
    ExceptionDto emailExiste(final EmailExiste exception) {
        return new ExceptionDto(
                CONFLICT,
                exception.getMessage()
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

    // @ResponseStatus(CONFLICT)
    // @ExceptionHandler(value = {DataIntegrityViolationException.class})
    // public @ResponseBody
    // ExceptionDto sqlExceptionHelper(final DataIntegrityViolationException exception) {

    //     return new ExceptionDto(
    //             CONFLICT,
    //             "Cela existe déjà"
    //     );
    // }


    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public @ResponseBody ExceptionDto handleNoHandlerFoundException(NoHandlerFoundException exception) {
        return new ExceptionDto(NOT_FOUND, "Erreur Inconnu");
    }


      @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public @ResponseBody ExceptionDto RoutenotFound(NoResourceFoundException exception) {
        return new ExceptionDto(NOT_FOUND, "Route not found");
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {IncorrectResultSizeDataAccessException.class})
    public @ResponseBody
    ExceptionDto incorrectResultSizeDataAccessException(final IncorrectResultSizeDataAccessException exception) {
        System.out.println(exception.getMessage());
        return new ExceptionDto(
                INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = LockedException.class)
    public @ResponseBody
    ExceptionDto lockedException(final LockedException exception) {
        return new ExceptionDto(
                FORBIDDEN,
                "Compte bloqué"
        );
    }


    @ExceptionHandler(FileStorageException.class)
    public @ResponseBody
    ExceptionDto handleFileStorageException(FileStorageException ex) {
        logger.error("File storage error: {}", ex.getMessage(), ex);
        return new ExceptionDto(INTERNAL_SERVER_ERROR, "FILE_STORAGE_ERROR" + ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public @ResponseBody
    ExceptionDto handleFileNotFoundException(FileNotFoundException ex) {
        logger.error("File not found: {}", ex.getMessage());
        return new ExceptionDto(NOT_FOUND, "FILE_NOT_FOUND " + ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public @ResponseBody
    ExceptionDto handleMaxSizeException(MaxUploadSizeExceededException ex) {
        logger.error("File size exceeded: {}", ex.getMessage());
        return new ExceptionDto(BAD_REQUEST, "Le fichier est trop volumineux");
    }


    @ExceptionHandler(FileValidationException.class)
    public @ResponseBody
    ExceptionDto handleGenericException(FileValidationException ex) {
        logger.error("validation file error: {}", ex.getMessage(), ex);
        return new ExceptionDto(BAD_REQUEST, "votre fichier ne repond pas au normes");
    }
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ExceptionDto handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ExceptionDto(INTERNAL_SERVER_ERROR, "Une erreur interne s'est produite");
    }

}
