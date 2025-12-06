package com.alpha.FindRide.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Service.DriverService;

@RestController
public class DriverController {
	
	@Autowired
	private DriverService ds;
	
	@PostMapping("/saveDriver")
	public ResponseStructure<Driver> saveDriver(@RequestBody RegisterDriverVehicleDTO rdto)
	{
		return ds.saveDriver(rdto);
	}
	
	@PostMapping("/findDriver")
    public ResponseStructure<Driver> findDriver(@RequestBody Map<String, Long> req) {
        long mobile = req.get("mobileno");
        return ds.findDriver(mobile);
    }
	
	 @PostMapping("/updateLocation")
	    public ResponseStructure<Driver> updateLocation(@RequestBody Map<String, String> req) {
	        long mobile = Long.parseLong(req.get("mobileno"));
	        String lat = req.get("latitude");
	        String lon = req.get("longitude");

	        return ds.updateLocation(mobile, lat, lon);
	    }
	 @DeleteMapping("/deleteDriver")
	 public ResponseStructure<String> deleteDriver(@RequestParam long mobileno) {
		 return ds.deleteDriver(mobileno);
	 }
}
