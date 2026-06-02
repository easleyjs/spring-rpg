package com.easleyjs.springrpg.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.easleyjs.springrpg.dto.ErrorResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(
                        "NOT FOUND",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(InvalidGameActionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidGameAction(
            InvalidGameActionException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(
                        "INVALID_GAME_ACTION",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientResources(
            InsufficientResourcesException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(
                   "INSUFFICIENT_RESOURCES",
                   ex.getMessage(),
                   request.getRequestURI()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                    "ACCESS_DENIED",
                    "You do not have permission to access this resource",
                    request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(
                        "VALIDATION_ERROR",
                        "Request validation failed.",
                        details,
                        request.getRequestURI()
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error("Unhandled exception at {}", request.getRequestURI(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(
                        "INTERNAL_SERVER_ERROR",
                        "An unexpected error occurred",
                        request.getRequestURI()
                ));
    }



}