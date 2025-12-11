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
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.FindCustomerDTO;
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
	
	@PostMapping("/findCustomer")
    public ResponseEntity<ResponseStructure<Customer>> findDriver(@RequestBody FindCustomerDTO fdto) {
        return cs.findCustomer(fdto);
    }
	
	@DeleteMapping("/deleteCustomer")
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(@RequestParam long mobileno){
		return cs.deleteCustomer(mobileno);
	}

	@PostMapping("/seeallAvailableVehicles")
	public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeallAvailableVehicles(@RequestParam long mobileno,@RequestParam String destinationCity)
	{
		return cs.seeallAvailableVehicles(mobileno,destinationCity);
	}
	
	@PostMapping("/seeBookingHistory")
	private ResponseEntity<ResponseStructure<List<Booking>>> seeBookingHistory(@RequestParam long mobileno)
	{
		return cs.seeBookingHistory(mobileno);
	}
	
	@PostMapping("/seeActiveBooking")
	private ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(@RequestParam long mobileno)
	{
		return cs.seeActiveBooking(mobileno);
	}
}
