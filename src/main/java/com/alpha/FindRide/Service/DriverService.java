package com.alpha.FindRide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Entity.Vehicle;
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

	public Driver saveDriver(RegisterDriverVehicleDTO rdto) {
		
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
		
		v.setId(d.getId());
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
		
		vr.save(v);
		d.setVehicle(v);
		
		return d;
	}
	
	
}
