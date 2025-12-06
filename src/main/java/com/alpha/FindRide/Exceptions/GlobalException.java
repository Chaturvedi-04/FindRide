package com.alpha.FindRide.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<?> driverNotFound(DriverNotFoundException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.NOT_FOUND.value());
        res.put("message", ex.getMessage());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LocationFetchException.class)
    public ResponseEntity<?> locationException(LocationFetchException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.BAD_GATEWAY.value());
        res.put("message", ex.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> global(Exception ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.put("message", ex.getMessage());
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}