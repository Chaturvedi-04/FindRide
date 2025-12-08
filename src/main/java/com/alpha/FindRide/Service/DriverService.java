package com.alpha.FindRide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.FindDriverDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Exceptions.DriverNotFoundException;
import com.alpha.FindRide.Exceptions.LocationFetchException;
import com.alpha.FindRide.Repository.DriverRepo;
import com.alpha.FindRide.Repository.VehicleRepo;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class DriverService {
	
	@Value("${locationiq.api.key}")
    private String apiKey;

	
	@Autowired
	private DriverRepo dr;
	
	@Autowired
	private VehicleRepo vr;

	public ResponseStructure<Driver> saveDriver(RegisterDriverVehicleDTO rdto) {
		
		Driver d = new Driver();
		d.setLicenseNo(rdto.getLicenseNo());
		d.setUpiid(rdto.getUpiid());
		d.setName(rdto.getDriverName());
		d.setAge(rdto.getAge());
		d.setMobileno(rdto.getMobileno());
		d.setGender(rdto.getGender());
		d.setMailid(rdto.getMailid());
		dr.save(d);
		
		Vehicle v = new Vehicle();
		
		v.setDriver(d);
		v.setName(rdto.getVehicleName());
		v.setVehicleNo(rdto.getVehicleNo());
		v.setType(rdto.getVehicleType());
		v.setModel(rdto.getModel());
		v.setCapacity(rdto.getVehicleCapacity());
				
		try {
            // LocationIQ Reverse Geocoding
            String url = "https://us1.locationiq.com/v1/reverse?key=" + apiKey +
                    "&lat=" + rdto.getLatitude() +
                    "&lon=" + rdto.getLongitude() +
                    "&format=json";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);

            String city = json.get("address").get("city").asString();
            
            v.setCurrentCity(city);

        } 
		
		catch (Exception e) {
            throw new RuntimeException("Error fetching location from LocationIQ: " + e.getMessage());
        }
		v.setAvailableStatus("Available");
		v.setPricePerKM(rdto.getPricePerKM());
		v.setAverageSpeed(rdto.getAverageSpeed());
		d.setVehicle(v);
		
		vr.save(v);
		
		ResponseStructure<Driver> rs= new ResponseStructure<Driver>();		
		rs.setStatuscode(HttpStatus.CREATED.value());
		rs.setMessage("Driver is saved");
		rs.setData(d);
		return rs;
	}
	
	public ResponseStructure<Driver> findDriver(FindDriverDTO fdto) {
	    Driver d = dr.findByMobileno(fdto.getMobileno());
	    if (d == null) {
	        throw new DriverNotFoundException();
	    }
        ResponseStructure<Driver> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.FOUND.value());
        rs.setMessage("Driver with MobileNo " + fdto.getMobileno() + " found");
        rs.setData(d);

        return rs;
	}

	public ResponseStructure<Driver> updateLocation(UpdateLocationDTO udto) {

		    Driver d = dr.findByMobileno(udto.getMobileno());
		    if (d == null) {
		        throw new DriverNotFoundException();
		    }

		    Vehicle v = d.getVehicle();
		    if (v == null) {
		        throw new RuntimeException("Vehicle not assigned for this driver");
		    }

		    try {
		        String url = "https://us1.locationiq.com/v1/reverse?key=" + apiKey +
		                "&lat=" + udto.getLatitude() +
		                "&lon=" + udto.getLongitude() +
		                "&format=json";

		        RestTemplate restTemplate = new RestTemplate();
		        String response = restTemplate.getForObject(url, String.class);

		        ObjectMapper mapper = new ObjectMapper();
		        JsonNode json = mapper.readTree(response);

		        String city = json.get("address").get("city").asString();
		        v.setCurrentCity(city);

		    } catch (Exception e) {
		        throw new LocationFetchException("Failed to fetch location from LocationIQ: " + e.getMessage());
		    }

		    vr.save(v);
		    d.setVehicle(v);

		    ResponseStructure<Driver> rs = new ResponseStructure<>();
		    rs.setStatuscode(HttpStatus.OK.value());
		    rs.setMessage("Driver updated successfully");
		    rs.setData(d);
		    return rs;

	}

	public ResponseStructure<String> deleteDriver(long mobileno) {
		Driver d = dr.findByMobileno(mobileno);
		if(d!=null) {
			dr.delete(d);
		}
		else {
		System.out.println("Driver Not Deleted");
		}
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Driver deleted");
	    rs.setData("Driver with MobileNo " + mobileno + " removed");

	    return rs;
	}
	
	
}
