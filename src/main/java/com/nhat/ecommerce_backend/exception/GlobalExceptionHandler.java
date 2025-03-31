package com.nhat.ecommerce_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,Object>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse("Xác thực thất bại!", HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", "Dữ liệu không hợp lệ!",
                "error", errors,
                "status", HttpStatus.BAD_REQUEST.value(),
                "timestamp", LocalDateTime.now()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException (Exception ex, WebRequest request) {
        return buildErrorResponse("Đã có lỗi xảy ra!", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> response = new HashMap<>(); // Sửa "Objects" -> "Object"
        response.put("message", message);
        response.put("status", status.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("path", request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(response);
    }
}
