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
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.DTO.RidedetailDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.DTO.VehicleDetailDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Exceptions.BookingNotFoundException;
import com.alpha.FindRide.Exceptions.CustomerNotFoundException;
import com.alpha.FindRide.Exceptions.InvalidDestinationLocationException;
import com.alpha.FindRide.Exceptions.LocationFetchException;
import com.alpha.FindRide.Exceptions.NoCurrentBookingException;
import com.alpha.FindRide.Exceptions.SameSourceAndDestinationException;
import com.alpha.FindRide.Repository.BookingRepo;
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
	
	@Autowired
	private BookingRepo br;

	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(RegisterCustomerDTO rdto) {
		Customer c = new Customer();
		c.setName(rdto.getName());
		c.setAge(rdto.getAge());
		c.setGender(rdto.getGender());
		c.setMobileno(rdto.getMobileno());
		c.setEmailid(rdto.getEmailid());
		try {
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

	public ResponseEntity<ResponseStructure<Customer>> findCustomer(long mobileno) {
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
        ResponseStructure<Customer> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.FOUND.value());
        rs.setMessage("Customer with MobileNo " + mobileno + " found");
        rs.setData(c);

        return new ResponseEntity<ResponseStructure<Customer>>(rs,HttpStatus.FOUND);
	}
	
	public ResponseEntity<ResponseStructure<String>> deleteCustomer(long mobileno){
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
		cr.delete(c);
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Customer deleted");
	    rs.setData("Customer with MobileNo " + mobileno + " removed");
	    return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeallAvailableVehicles(long mobileno, String destinationCity) {
		
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
		String sourceLoc = c.getCurrentloc();
		String destionationLoc = destinationCity;
		double distance;
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        ObjectMapper mapper = new ObjectMapper();

	        String srcUrl = "https://us1.locationiq.com/v1/search?key=" + apiKey +
	                "&q=" + sourceLoc + "&format=json";

	        JsonNode srcJsonArray = mapper.readTree(restTemplate.getForObject(srcUrl, String.class));
	        if (srcJsonArray.isEmpty()) {
	            throw new RuntimeException("Invalid source city returned null data.");
	        }
	        JsonNode srcJson = srcJsonArray.get(0);

	        double srcLat = srcJson.get("lat").asDouble();
	        double srcLon = srcJson.get("lon").asDouble();

	        String destUrl = "https://us1.locationiq.com/v1/search?key=" + apiKey +
	                "&q=" + destionationLoc + "&format=json";

	        JsonNode destJsonArray = mapper.readTree(restTemplate.getForObject(destUrl, String.class));

	        if (destJsonArray == null || destJsonArray.isEmpty()) {
	            throw new InvalidDestinationLocationException();
	        }

	        JsonNode destJson = destJsonArray.get(0);
	        double destLat = destJson.get("lat").asDouble();
	        double destLon = destJson.get("lon").asDouble();
	        
	        if (srcLat == destLat && srcLon == destLon) {
	            throw new SameSourceAndDestinationException();
	        }

	        String directionUrl = String.format(
	            "https://us1.locationiq.com/v1/directions/driving/%f,%f;%f,%f?key=%s",
	            srcLon, srcLat, destLon, destLat, apiKey
	        );

	        JsonNode dirJson = mapper.readTree(restTemplate.getForObject(directionUrl, String.class));
	        
	        double distanceInMeters = dirJson.get("routes").get(0).get("distance").asDouble();
	        distance = distanceInMeters / 1000;
	        
	    }
	    catch (Exception e) {
	        throw new RuntimeException("Error fetching distance: " + e.getMessage());
	    }
	    double penaltyamount = 0;
	    double penaltyrate = 0;
	    
        if (c.getPenaltyCount() > 1) {
            penaltyrate = (c.getPenaltyCount() - 1) * 0.10; 
        }

	    AvailableVehicleDTO avd = new AvailableVehicleDTO();
	    avd.setAvailableVehicles(new ArrayList<>());
	    
	    List<Vehicle> availableVehicles = vr.findByCurrentCity(sourceLoc);
	    
	    for(Vehicle v: availableVehicles)
	    {
	    	double fare = v.getPricePerKM() * distance;
	    	 double totalFare = fare + (fare * penaltyrate);
	    	
	    	penaltyamount = fare * penaltyrate;

	    	double estimatedTime = distance/v.getAverageSpeed();
	    	VehicleDetailDTO vd = new VehicleDetailDTO(v,totalFare,estimatedTime);
	    	avd.getAvailableVehicles().add(vd);
	    }
	    avd.setPenaltyamount(penaltyamount);
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
	
	public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory(long mobileno) {
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
		List<Booking> blist = c.getBookingList();
		List<RidedetailDTO> ridedetaildto = new ArrayList<RidedetailDTO>();
		double totalamount=0;
		
		for(Booking b : blist)
		{
			RidedetailDTO rdto = new RidedetailDTO();
			rdto.setFromloc(b.getSourceLoc());
			rdto.setToloc(b.getDestinationLoc());
			rdto.setDistance(b.getDistanceTravelled());
			rdto.setFare(b.getFare());
			ridedetaildto.add(rdto);
			totalamount+=b.getFare();
		}
		BookingHistoryDTO bhdto = new BookingHistoryDTO();
		bhdto.setHistory(ridedetaildto);
		bhdto.setTotalamount(totalamount);
		ResponseStructure<BookingHistoryDTO> rs = new ResponseStructure<BookingHistoryDTO>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Booking History is Fetched");
		rs.setData(bhdto);
		return new ResponseEntity<ResponseStructure<BookingHistoryDTO>>(rs,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking(long mobileno) {
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
	    Booking b = vr.findActiveBookingOfCustomer(mobileno).orElseThrow(()->new NoCurrentBookingException());
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

	    Customer c = cr.findByMobileno(udto.getMobileno()).orElseThrow(()->new CustomerNotFoundException());
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
	
	public ResponseEntity<ResponseStructure<Booking>> cancelbooking(int customerid, int bookingid) {
		
		Customer c = cr.findById(customerid).orElseThrow(()->new CustomerNotFoundException());
		Booking book = br.findById(bookingid).orElseThrow(()->new BookingNotFoundException());

//		Vehicle v= vr.findById(v.getId()).orElseThrow(()->new VehicleNotFoundException());
				
		if (book.getCust().getId() != customerid) {
			throw new BookingNotFoundException(); 
		}
		if (book.getBookingStatus().equalsIgnoreCase("CANCELLED")) {
		        throw new IllegalStateException("Booking already cancelled");
		}

		book.setBookingStatus("CANCELLED By CUSTOMER");
		book.setPaymentStatus("NOT PAID");
		
		if (c.getPenaltyCount() >= 1) {      
		        c.setPenaltyCount(c.getPenaltyCount() + 1);
		 } else {       
		        c.setPenaltyCount(1);
		 }
	    c.setBookingStatus(false);
	    
		br.save(book);
		cr.save(c);

		Vehicle v = book.getVehicle();
//		Driver d = book.getDriver();

		v.setAvailableStatus("AVAILABLE");
//		d.setStatus("AVAILABLE");

		vr.save(v);
		 
		ResponseStructure<Booking> rs = new ResponseStructure<Booking>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Booking cancelled successfully");
		rs.setData(book);
		return new ResponseEntity<ResponseStructure<Booking>>(rs,HttpStatus.OK);
	}
}
