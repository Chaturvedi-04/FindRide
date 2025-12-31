package com.alpha.FindRide.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alpha.FindRide.ResponseStructure;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> driverNotFound(DriverNotFoundException ex) {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Driver NOT FOUND");
		rs.setData("Driver NOT FOUND");
		return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> vehicleNotFound(VehicleNotFoundException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_FOUND.value());
    	rs.setMessage("Vehicle NOT FOUND");
    	rs.setData("Vehicle NOT FOUND");
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> customerNotFound(CustomerNotFoundException ex) {
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Customer NOT FOUND");
		rs.setData("Customer NOT FOUND");
		return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> bookingNotFoundException(BookingNotFoundException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_FOUND.value());
    	rs.setMessage("Booking NOT FOUND");
    	rs.setData("Booking NOT FOUND");
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LocationFetchException.class)
    public ResponseEntity<ResponseStructure<String>> locationException(LocationFetchException ex) {

        ResponseStructure<String> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.BAD_GATEWAY.value());
        rs.setMessage(ex.getMessage());
        rs.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> global(Exception ex) {

        ResponseStructure<String> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        rs.setMessage(ex.getMessage());
        rs.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(InvalidDestinationLocationException.class)
    public ResponseEntity<ResponseStructure<String>> invalidDestinationException(InvalidDestinationLocationException ex) {
        ResponseStructure<String> rs = new ResponseStructure<String>();
        rs.setStatuscode(HttpStatus.BAD_GATEWAY.value());
        rs.setMessage(ex.getMessage());
        rs.setData(null);
        return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.BAD_GATEWAY);
    }
    
    @ExceptionHandler(NoCurrentBookingException.class)
    public ResponseEntity<ResponseStructure<String>> NoCurrentBookingException(NoCurrentBookingException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_FOUND.value());
    	rs.setMessage("No Current Booking");
    	rs.setData(null);
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(SameSourceAndDestinationException.class)
    public ResponseEntity<ResponseStructure<String>> sameSourceAndDestinationException(SameSourceAndDestinationException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_ACCEPTABLE.value());
    	rs.setMessage("Source and destination cannot be the same");
    	rs.setData("Source and destination cannot be the same");
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_ACCEPTABLE);
    }
    
    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ResponseStructure<String>> invalidOtpException(InvalidOtpException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_ACCEPTABLE.value());
    	rs.setMessage("Invalid OTP");
    	rs.setData("Invalid OTP");
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_ACCEPTABLE);
    }
    
    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> AppUserNotFoundException(AppUserNotFoundException ex) {
    	ResponseStructure<String> rs = new ResponseStructure<String>();
    	rs.setStatuscode(HttpStatus.NOT_ACCEPTABLE.value());
    	rs.setMessage("Invalid user mobile number");
    	rs.setData("Invalid user mobile number");
    	return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.NOT_ACCEPTABLE);
    }
    
}