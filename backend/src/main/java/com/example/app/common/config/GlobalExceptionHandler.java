package com.example.app.common.config;

import com.example.app.common.exception.BusinessException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse error = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        ErrorResponse error = new ErrorResponse("E999", message);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        String detail = e.getClass().getSimpleName() + ": " + e.getMessage();
        if (e.getCause() != null) detail += " | Caused by: " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage();
        ErrorResponse error = new ErrorResponse("E999", detail);
        return ResponseEntity.internalServerError().body(error);
    }

    @Data
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
