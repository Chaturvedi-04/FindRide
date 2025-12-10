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
import com.alpha.FindRide.DTO.FindDriverDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Service.DriverService;

@RestController
@RequestMapping("/driver")
public class DriverController {
	
	@Autowired
	private DriverService ds;
	
	@PostMapping("/saveDriver")
	public ResponseStructure<Driver> saveDriver(@RequestBody RegisterDriverVehicleDTO rdto)
	{
		return ds.saveDriver(rdto);
	}
	
	@GetMapping("/findDriver")
    public ResponseStructure<Driver> findDriver(@RequestBody FindDriverDTO fdto) {
        return ds.findDriver(fdto);
    }
	
	 @PostMapping("/updateLocation")
	    public ResponseStructure<Driver> updateLocation(@RequestBody UpdateLocationDTO udto) {
	        return ds.updateLocation(udto);
	    }
	 @DeleteMapping("/deleteDriver")
	 public ResponseStructure<String> deleteDriver(@RequestParam long mobileno) {
		 return ds.deleteDriver(mobileno);
	 }
}
