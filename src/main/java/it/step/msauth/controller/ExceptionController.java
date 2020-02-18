package it.step.msauth.controller;

import it.step.msauth.model.ExceptionDto;
import it.step.msauth.model.exception.AuthUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException exc) {
        exc.printStackTrace();
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.valueOf(500));
    }

    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<ExceptionDto> handleAuthUserException(AuthUserException exc) {
        return new ResponseEntity<>(new ExceptionDto(exc.getMessage()), HttpStatus.valueOf(401));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
