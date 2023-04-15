package com.corcino.personbook.error.handler;

import com.corcino.personbook.error.StandardError;
import com.corcino.personbook.error.ValidationError;
import com.corcino.personbook.error.exception.BadRequestException;
import com.corcino.personbook.error.exception.ForbiddenException;
import com.corcino.personbook.error.exception.NotFoundException;
import com.corcino.personbook.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> handleNotFound(NotFoundException notFoundException) {
        StandardError error = StandardError.builder()
                .title("Object Not Found Exception. Check documentation.")
                .status(HttpStatus.NOT_FOUND.value())
                .errorMessage(notFoundException.getMessage())
                .developerMessage(notFoundException.getClass().getName())
                .dateTime(getDateTime())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> handleBadRequest(BadRequestException badRequestException) {
        StandardError error = StandardError.builder()
                .title("Bad Request Exception.")
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMessage(badRequestException.getMessage())
                .developerMessage(badRequestException.getClass().getName())
                .dateTime(getDateTime())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleUnauthorizedException(UnauthorizedException unauthorizedException) {
        StandardError error = StandardError.builder()
                .title("API access unauthorized.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(unauthorizedException.getMessage())
                .developerMessage(unauthorizedException.getClass().getName())
                .dateTime(getDateTime())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<StandardError> handleForbiddenException(ForbiddenException forbiddenException) {
        StandardError error = StandardError.builder()
                .title("API access forbidden.")
                .status(HttpStatus.FORBIDDEN.value())
                .errorMessage(forbiddenException.getMessage())
                .developerMessage(forbiddenException.getClass().getName())
                .dateTime(getDateTime())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentException) {
        List<ValidationError> standardErrors = new ArrayList<>();
        List<FieldError> fieldErrors = methodArgumentException.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            ValidationError validationError = ValidationError.builder()
                    .title("Bad Request Exception. Invalid fields.")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errorMessage(fieldError.getDefaultMessage())
                    .developerMessage(fieldError.getClass().getName())
                    .dateTime(getDateTime())
                    .field(fieldError.getField())
                    .build();
            standardErrors.add(validationError);
            log.error("Erro de validação no campo " + fieldError.getField() + " para se criar ou atualizar recurso");
        });

        return new ResponseEntity<>(standardErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleInternalException(Exception exception) {
        StandardError error = StandardError.builder()
                .title("Internal error in server")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("Internal error in server")
                .developerMessage(exception.getClass().getName())
                .dateTime(getDateTime())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Error.class)
    public ResponseEntity<StandardError> handleInternalError(Error err) {
        StandardError error = StandardError.builder()
                .title("Internal error in server ")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("Internal error in server")
                .developerMessage(err.getClass().getName())
                .dateTime(getDateTime())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

}
