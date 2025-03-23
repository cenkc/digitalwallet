package com.cenkc.digitalwallet.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception e) {
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .time(LocalDateTime.now())
                        .errorMessage(e.getMessage())
                        .uri(req.getRequestURI())
                        .method(req.getMethod())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(HttpServletRequest req,
                                                            HttpServletResponse res,
                                                            BadRequestException e) {
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .time(LocalDateTime.now())
                        .errorMessage(e.getMessage())
                        .uri(req.getRequestURI())
                        .method(req.getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(HttpServletRequest req,
                                                                        HttpServletResponse res,
                                                                        MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .time(LocalDateTime.now())
                        .errorMessage("Constraint(s) Validation Failed")
                        .uri(req.getRequestURI())
                        .method(req.getMethod())
                        .errors(errorMap)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(HttpServletRequest req,
                                                                        HttpServletResponse res,
                                                                        DataIntegrityViolationException e) {

        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .time(LocalDateTime.now())
                        // TODO : e.getMessage() may expose internal data structure
                        //  (e.g. while validating TCKN, Customer table fields appears in the message).
                        //  So, it should be handled properly.
                        .errorMessage(String.format("Data Integrity Violation : %s", e.getMessage()))
                        .uri(req.getRequestURI())
                        .method(req.getMethod())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
