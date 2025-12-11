package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.BookingDTO;
import com.alpha.FindRide.Service.BookingService;

@RestController
public class BookingController {
	
	@Autowired
	private BookingService bs;
	
	@PostMapping("/bookVehicle")
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> bookVehicle(@RequestParam long mobileno,@RequestBody BookingDTO bookingdto)
	{
		return bs.bookVehicle(mobileno, bookingdto);
	}
}
