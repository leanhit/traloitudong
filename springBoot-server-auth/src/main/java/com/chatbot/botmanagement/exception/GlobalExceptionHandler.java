package com.chatbot.botmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
    //     Map<String, Object> body = new HashMap<>();
    //     body.put("error", "RuntimeException");
    //     body.put("message", ex.getMessage());
    //     body.put("timestamp", LocalDateTime.now());
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    // }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
    //     Map<String, Object> body = new HashMap<>();
    //     body.put("error", "Exception");
    //     body.put("message", ex.getMessage());
    //     body.put("timestamp", LocalDateTime.now());
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    // }

    // @ExceptionHandler(HttpClientErrorException.class)
    // public ResponseEntity<Map<String, Object>> handleHttpClientError(HttpClientErrorException ex) {
    //     Map<String, Object> body = new HashMap<>();
    //     body.put("error", "HttpClientErrorException");
    //     body.put("status", ex.getStatusCode().value());
    //     body.put("message", ex.getResponseBodyAsString());
    //     body.put("timestamp", LocalDateTime.now());
    //     return ResponseEntity.status(ex.getStatusCode()).body(body);
    // }

}

