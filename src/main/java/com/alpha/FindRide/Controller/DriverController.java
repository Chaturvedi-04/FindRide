package com.alpha.FindRide.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDriverDTO;
import com.alpha.FindRide.DTO.FindDriverDTO;
import com.alpha.FindRide.DTO.PaymentDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Service.DriverService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/driver")
public class DriverController {
	
	@Autowired
	private DriverService ds;
	
	@PostMapping("/saveDriver")
	public ResponseEntity<ResponseStructure<Driver>> saveDriver(@RequestBody RegisterDriverVehicleDTO rdto)
	{
		return ds.saveDriver(rdto);
	}
	
	@PostMapping("/findDriver")
    public ResponseEntity<ResponseStructure<Driver>> findDriver(@RequestBody FindDriverDTO fdto) {
        return ds.findDriver(fdto);
    }
	
	 @PostMapping("/updateLocation")
	 public ResponseEntity<ResponseStructure<Driver>> updateLocation(@RequestBody UpdateLocationDTO udto) {
	     return ds.updateLocation(udto);
	 }
	 
	 @DeleteMapping("/deleteDriver")
	 public ResponseEntity<ResponseStructure<String>> deleteDriver(@RequestParam long mobileno) {
		 return ds.deleteDriver(mobileno);
	 }
	
	 @PostMapping("/seeBookingHistory")
	 public ResponseEntity<ResponseStructure<List<Booking>>> seeBookingHistory(@RequestParam long mobileno)
	 {
		return ds.seeBookingHistory(mobileno);
	 }
	 
	 @PostMapping("/seeActiveBooking")
	 public ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>> seeActiveBooking(@RequestParam long mobileno)
	 {
		return ds.seeActiveBooking(mobileno);
	 }
	 
	 @PostMapping("/completeride/payByCash")
	 public ResponseEntity<ResponseStructure<PaymentDTO>> completePayment(@RequestParam int bookingid,@RequestParam String paytype)
	 {
		 return ds.completePayment(bookingid, paytype);
	 }
}
