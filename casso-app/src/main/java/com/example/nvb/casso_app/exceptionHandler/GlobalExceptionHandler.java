package com.example.nvb.casso_app.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleWebClientError(WebClientResponseException ex) {
        // Parse JSON body từ Cas API
        String body = ex.getResponseBodyAsString();

        if (body.contains("GRANT_LOGIN_REQUIRED")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "GRANT_LOGIN_REQUIRED",
                    "message", "Người dùng cần đăng nhập lại thông qua Update Mode"
            ));
        }

        return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                "error", "CAS_API_ERROR",
                "message", body
        ));
    }
}
