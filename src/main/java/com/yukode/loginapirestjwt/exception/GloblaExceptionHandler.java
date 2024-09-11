package com.yukode.loginapirestjwt.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GloblaExceptionHandler {
    
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), ex.getMessage(), request.getDescription(false), LocalDateTime.now());
        
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(UserInvalidCredentialException.class)
    public ResponseEntity<ApiError> handleUserInvalidCredentialException(UserInvalidCredentialException ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getDescription(false), LocalDateTime.now());
        
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> HandleValidationException(MethodArgumentNotValidException ex, WebRequest request){
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), errorMessage, request.getDescription(false), LocalDateTime.now());
        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> HandleAllException(Exception ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getDescription(false), LocalDateTime.now());
        
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
