package com.alpha.FindRide.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDriverDTO;
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.FindDriverDTO;
import com.alpha.FindRide.DTO.PaymentDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.DTO.upiPaymentDTO;
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
	 public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory(@RequestParam long mobileno)
	 {
		return ds.seeBookingHistory(mobileno);
	 }
	 
	 @PostMapping("/seeActiveBooking")
	 public ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>> seeActiveBooking(@RequestParam long mobileno)
	 {
		return ds.seeActiveBooking(mobileno);
	 }
	 
	 @PostMapping("/completeride/payByCash")
	 public ResponseEntity<ResponseStructure<PaymentDTO>> completePayment(@RequestParam int bookingid,@RequestParam String paytype,@RequestParam int otp)
	 {
		 return ds.completePayment(bookingid, paytype,otp);
	 }
	 
	 @PostMapping("/completeride/payByUpi")
	 public ResponseEntity<ResponseStructure<upiPaymentDTO>> paymentService(@RequestParam int bookingid,@RequestParam String paytype)
	 {
		 return ds.paymentService(bookingid, paytype);
	 }
	 
	 @GetMapping("/ridecompleted/paymentconfirmed")
	 public ResponseEntity<ResponseStructure<PaymentDTO>> confrimPaymentCollection(@RequestParam int bookingid,@RequestParam String paytype,@RequestParam int otp)
	 {
		 return ds.confrimPaymentCollection(bookingid,paytype,otp);
	 }
	 
	 @PostMapping("/cancelbooking")
	 public ResponseEntity<ResponseStructure<Booking>> cancelbooking(@RequestParam int driverid,@RequestParam int bookingid)
	 {
		 return ds.cancelbooking(driverid,bookingid);
	 }
	 
	 @PostMapping("/startride")
	 public ResponseEntity<ResponseStructure<String>> startride(@RequestParam int otp,@RequestParam int bookingid)
	 {
		 return ds.startride(otp,bookingid);
	 }
}