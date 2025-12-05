package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Service.DriverService;

@RestController
public class DriverController {
	
	@Autowired
	private DriverService ds;
	
	@PostMapping("/saveDriver")
	public Driver saveDriver(@RequestBody RegisterDriverVehicleDTO rdto)
	{
		return ds.saveDriver(rdto);
	}
}
