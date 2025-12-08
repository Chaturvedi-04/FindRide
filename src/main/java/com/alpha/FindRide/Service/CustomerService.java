package com.alpha.FindRide.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.FindCustomerDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.DTO.VehicleDetailDTO;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Exceptions.CustomerNotFoundException;
import com.alpha.FindRide.Exceptions.InvalidDestinationLocationException;
import com.alpha.FindRide.Repository.CustomerRepo;
import com.alpha.FindRide.Repository.VehicleRepo;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CustomerService {
	
	@Value("${locationiq.api.key}")
    private String apiKey;
	
	@Autowired
	private CustomerRepo cr;
	
	@Autowired
	private VehicleRepo vr;

	public ResponseStructure<Customer> saveCustomer(RegisterCustomerDTO rdto) {
		Customer c = new Customer();
		c.setName(rdto.getName());
		c.setAge(rdto.getAge());
		c.setGender(rdto.getGender());
		c.setMobileno(rdto.getMobileno());
		c.setEmailid(rdto.getEmailid());
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
            
            c.setCurrentloc(city);

        } 
		
		catch (Exception e) {
            throw new RuntimeException("Error fetching location from LocationIQ: " + e.getMessage());
        }
		cr.save(c);
		ResponseStructure<Customer> rs = new ResponseStructure<Customer>();
		rs.setStatuscode(HttpStatus.CREATED.value());
		rs.setMessage("Customer is saved");
		rs.setData(c);
		return rs;
	}

	public ResponseStructure<Customer> findCustomer(FindCustomerDTO cdto) {
		Customer c = cr.findByMobileno(cdto.getMobileno());
		if(c==null)
		{
			throw new CustomerNotFoundException();
		}
        ResponseStructure<Customer> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.FOUND.value());
        rs.setMessage("Customer with MobileNo " + cdto.getMobileno() + " found");
        rs.setData(c);

        return rs;
	}
	
	public ResponseStructure<String> deleteCustomer(long mobileno){
		Customer c = cr.findByMobileno(mobileno);
		if(c == null) {
			throw new CustomerNotFoundException();
		}
		cr.delete(c);
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Customer deleted");
	    rs.setData("Customer with MobileNo " + mobileno + " removed");
	    return rs;
	}

	public ResponseStructure<AvailableVehicleDTO> seeallAvailableVehicles(long mobileno, String destinationCity) {
		
		Customer c = cr.findByMobileno(mobileno);
	    if (c == null) {
	        throw new CustomerNotFoundException();
	    }
		String sourceLoc = c.getCurrentloc();
		String destionationLoc = destinationCity;
		double distance;

	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        ObjectMapper mapper = new ObjectMapper();

	        // Step 1: Get source coordinates
	        String srcUrl = "https://us1.locationiq.com/v1/search?key=" + apiKey +
	                "&q=" + sourceLoc + "&format=json";

	        JsonNode srcJsonArray = mapper.readTree(restTemplate.getForObject(srcUrl, String.class));
	        if (srcJsonArray.isEmpty()) {
	            throw new RuntimeException("Invalid source city returned null data.");
	        }
	        JsonNode srcJson = srcJsonArray.get(0);

	        double srcLat = srcJson.get("lat").asDouble();
	        double srcLon = srcJson.get("lon").asDouble();

	        // Step 2: Get destination coordinates
	        String destUrl = "https://us1.locationiq.com/v1/search?key=" + apiKey +
	                "&q=" + destionationLoc + "&format=json";

	        JsonNode destJsonArray = mapper.readTree(restTemplate.getForObject(destUrl, String.class));

	        // 🚨 Check if destination is invalid
	        if (destJsonArray == null || destJsonArray.isEmpty()) {
	            throw new InvalidDestinationLocationException();
	        }

	        JsonNode destJson = destJsonArray.get(0);
	        double destLat = destJson.get("lat").asDouble();
	        double destLon = destJson.get("lon").asDouble();
	        

	        // Step 3: Use Directions API to get distance
	        String directionUrl = String.format(
	            "https://us1.locationiq.com/v1/directions/driving/%f,%f;%f,%f?key=%s",
	            srcLon, srcLat, destLon, destLat, apiKey
	        );

	        JsonNode dirJson = mapper.readTree(restTemplate.getForObject(directionUrl, String.class));
	        
	        double distanceInMeters = dirJson.get("routes").get(0).get("distance").asDouble();
	        distance = distanceInMeters / 1000; // Convert meters → kilometers
	    }
	    catch (Exception e) {
	        throw new RuntimeException("Error fetching distance: " + e.getMessage());
	    }
	    AvailableVehicleDTO avd = new AvailableVehicleDTO();
	    avd.setAvailableVehicles(new ArrayList<>());
	    
	    List<Vehicle> availableVehicles = vr.findByCurrentCity(sourceLoc);
	    
	    for(Vehicle v: availableVehicles)
	    {
	    	double fare = v.getPricePerKM()*distance;
	    	double estimatedTime = distance/v.getAverageSpeed();
	    	VehicleDetailDTO vd = new VehicleDetailDTO(v,fare,estimatedTime);
	    	avd.getAvailableVehicles().add(vd);
	    }
	    
	    avd.setC(c);
	    avd.setDistance(distance);
	    avd.setSourceloc(sourceLoc);
	    avd.setDestinationloc(destionationLoc);
	    
	    ResponseStructure<AvailableVehicleDTO> rs = new ResponseStructure<AvailableVehicleDTO>();
	    
	    rs.setStatuscode(HttpStatus.ACCEPTED.value());
	    rs.setMessage("Available Cars");
	    rs.setData(avd);
	    return rs;
	}
}
