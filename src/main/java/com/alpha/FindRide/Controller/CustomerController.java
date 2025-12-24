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
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Service.CustomerService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService cs;
	
	@PostMapping("/saveCustomer")
	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(@RequestBody RegisterCustomerDTO rdto)
	{
		return cs.saveCustomer(rdto);
	}
	
	@GetMapping("/auth/findCustomer")
    public ResponseEntity<ResponseStructure<Customer>> findDriver(@RequestParam long mobileno) {
        return cs.findCustomer(mobileno);
    }
	
	@DeleteMapping("/auth/deleteCustomer")
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(@RequestParam long mobileno){
		return cs.deleteCustomer(mobileno);
	}

	@GetMapping("/auth/seeallAvailableVehicles")
	public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeallAvailableVehicles(@RequestParam long mobileno,@RequestParam String destinationCity)
	{
		return cs.seeallAvailableVehicles(mobileno,destinationCity);
	}
	
	@GetMapping("/auth/seeBookingHistory")
	public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory(@RequestParam long mobileno)
	{
		return cs.seeBookingHistory(mobileno);
	}
	
	@GetMapping("/auth/seeActiveBooking")
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(@RequestParam long mobileno)
	{
		return cs.seeActiveBooking(mobileno);
	}
	
	 @PostMapping("/auth/cancelbooking")
	 public ResponseEntity<ResponseStructure<Booking>> cancelBooking (@RequestParam int customerid,@RequestParam int bookingid)
	 {
		 return cs.cancelbooking(customerid, bookingid);
	 }
}
