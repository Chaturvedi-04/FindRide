package com.alpha.FindRide.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.FindCustomerDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.DTO.VehicleDetailDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Exceptions.CustomerNotFoundException;
import com.alpha.FindRide.Exceptions.InvalidDestinationLocationException;
import com.alpha.FindRide.Exceptions.LocationFetchException;
import com.alpha.FindRide.Exceptions.NoCurrentBookingException;
import com.alpha.FindRide.Exceptions.SameSourceAndDestinationException;
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

	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(RegisterCustomerDTO rdto) {
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
		return new ResponseEntity<ResponseStructure<Customer>>(rs,HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<Customer>> findCustomer(FindCustomerDTO cdto) {
		Customer c = cr.findByMobileno(cdto.getMobileno());
		if(c==null)
		{
			throw new CustomerNotFoundException();
		}
        ResponseStructure<Customer> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.FOUND.value());
        rs.setMessage("Customer with MobileNo " + cdto.getMobileno() + " found");
        rs.setData(c);

        return new ResponseEntity<ResponseStructure<Customer>>(rs,HttpStatus.FOUND);
	}
	
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(long mobileno){
		Customer c = cr.findByMobileno(mobileno);
		if(c == null) {
			throw new CustomerNotFoundException();
		}
		cr.delete(c);
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Customer deleted");
	    rs.setData("Customer with MobileNo " + mobileno + " removed");
	    return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeallAvailableVehicles(long mobileno, String destinationCity) {
		
		Customer c = cr.findByMobileno(mobileno);
	    if (c == null) {
	        throw new CustomerNotFoundException();
	    }
		String sourceLoc = c.getCurrentloc();
		String destionationLoc = destinationCity;
		double distance;
//		if(sourceLoc == destionationLoc)
//		{
//			throw new SameSourcedestinationLocationException();
//		}

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
	        
	        // 🚨 NEW STEP: Check if source and destination are the same
	        if (srcLat == destLat && srcLon == destLon) {
	            throw new SameSourceAndDestinationException();
	        }

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
	    return new ResponseEntity<ResponseStructure<AvailableVehicleDTO>>(rs,HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ResponseStructure<List<Booking>>> seeBookingHistory(long mobileno) {
		List<Booking> blist = vr.findAllCompletedBookingsOfCustomer(mobileno);
		ResponseStructure<List<Booking>> rs = new ResponseStructure<List<Booking>>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Booking List is Fetched");
		rs.setData(blist);
		return new ResponseEntity<ResponseStructure<List<Booking>>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(long mobileno) {
		Customer c = cr.findByMobileno(mobileno);
	    if (c == null) {
	        throw new CustomerNotFoundException();
	    }
	    Booking b = vr.findActiveBookingOfCustomer(mobileno);
	    if(b==null)
	    {
	    	throw new NoCurrentBookingException();
	    }
	    ActiveBookingDTO abdto = new ActiveBookingDTO();
	    abdto.setBooking(b);
	    abdto.setCustname(c.getName());
	    abdto.setCustmobno(mobileno);
	    abdto.setCurrentlocation(c.getCurrentloc());
	    
	    ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<ActiveBookingDTO>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Current Booking is Fetched");
		rs.setData(abdto);
		return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<Customer>> updateLocationDriver(UpdateLocationDTO udto) {

	    Customer c = cr.findByMobileno(udto.getMobileno());
	    if (c == null) {
	        throw new CustomerNotFoundException();
	    }
	    try {
	        String url = "https://us1.locationiq.com/v1/reverse?key=" + apiKey +
	                "&lat=" + udto.getLatitude() +
	                "&lon=" + udto.getLongitude() +
	                "&format=json";

	        RestTemplate restTemplate = new RestTemplate();
	        ObjectMapper mapper = new ObjectMapper();
	        
	        JsonNode json = mapper.readTree(restTemplate.getForObject(url, String.class));

	        String city = json.get("address").get("city").asString();
	        c.setCurrentloc(city);

	    } catch (Exception e) {
	        throw new LocationFetchException("Failed to fetch location from LocationIQ: " + e.getMessage());
	    }

	    cr.save(c);

	    ResponseStructure<Customer> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Customer location updated successfully");
	    rs.setData(c);
	    return new ResponseEntity<ResponseStructure<Customer>>(rs,HttpStatus.OK);
	}
}
