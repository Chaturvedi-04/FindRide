package com.alpha.FindRide.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alpha.FindRide.ResponseStructure;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseStructure<String> driverNotFound(DriverNotFoundException ex) {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Driver NOT FOUND");
		rs.setData("Driver NOT FOUND");
		return rs;
    }
    
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseStructure<String> customerNotFound(CustomerNotFoundException ex) {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Customer NOT FOUND");
		rs.setData("Customer NOT FOUND");
		return rs;
    }

    @ExceptionHandler(LocationFetchException.class)
    public ResponseEntity<ResponseStructure<String>> locationException(LocationFetchException ex) {

        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatuscode(HttpStatus.BAD_GATEWAY.value());
        response.setMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> global(Exception ex) {

        ResponseStructure<String> response = new ResponseStructure<>();
        response.setStatuscode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}