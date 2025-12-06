package com.alpha.FindRide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.FindCustomerDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Exceptions.CustomerNotFoundException;
import com.alpha.FindRide.Exceptions.DriverNotFoundException;
import com.alpha.FindRide.Repository.CustomerRepo;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CustomerService {
	
	@Value("${locationiq.api.key}")
    private String apiKey;
	
	@Autowired
	private CustomerRepo cr;

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
		
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Customer deleted");
	    rs.setData("Customer with MobileNo " + mobileno + " removed");
	    return rs;

		
		
		
	}

}
