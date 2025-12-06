package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.FindCustomerDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService cs;
	
	@PostMapping("/saveCustomer")
	public ResponseStructure<Customer> saveCustomer(@RequestBody RegisterCustomerDTO rdto)
	{
		return cs.saveCustomer(rdto);
	}
	
	@GetMapping("/findCustomer")
    public ResponseStructure<Customer> findDriver(@RequestBody FindCustomerDTO fdto) {
        return cs.findCustomer(fdto);
    }
	
	@DeleteMapping("/deleteCustomer")
	public ResponseStructure<String> deleteCustomer(@RequestParam long mobileno){
		return cs.deleteCustomer(mobileno);
	}

	@PostMapping("/seeallAvailableVehicles")
	public ResponseStructure<AvailableVehicleDTO> seeallAvailableVehicles(@RequestParam long mobileno,@RequestParam String destinationCity)
	{
		return cs.seeallAvailableVehicles(mobileno,destinationCity);
	}
}
