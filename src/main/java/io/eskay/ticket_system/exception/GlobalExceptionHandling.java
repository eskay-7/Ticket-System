package io.eskay.ticket_system.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException (ResourceNotFoundException e) {
        var error = new ExceptionResponse(
                HttpStatusCode.valueOf(404).value(),
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleException (IllegalArgumentException e) {
        var error = new ExceptionResponse(
                HttpStatusCode.valueOf(400).value(),
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException (MethodArgumentNotValidException e) {
        var errorMessages = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList()
                .toString()
                .replace("[","")
                .replace("]","");
        var error = new ExceptionResponse(
                HttpStatusCode.valueOf(400).value(),
                HttpStatus.BAD_REQUEST,
                errorMessages,
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleException(ConstraintViolationException e) {
        String errorMessages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList()
                .toString()
                .replace("[","")
                .replace("]","");

        ExceptionResponse error = new ExceptionResponse(
                HttpStatusCode.valueOf(400).value(),
                HttpStatus.BAD_REQUEST,
                errorMessages,
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpMessageNotReadableException e) {

        ExceptionResponse error = new ExceptionResponse(
                HttpStatusCode.valueOf(400).value(),
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException (UsernameNotFoundException e) {
        var error = new ExceptionResponse(
                HttpStatusCode.valueOf(404).value(),
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccountAlreadyExistsException e) {
        var error = new ExceptionResponse(
                HttpStatusCode.valueOf(400).value(),
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                Timestamp.valueOf(LocalDateTime.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
