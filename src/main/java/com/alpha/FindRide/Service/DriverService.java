package com.alpha.FindRide.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDriverDTO;
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.PaymentDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.RidedetailDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.DTO.upiPaymentDTO;
import com.alpha.FindRide.Entity.AppUser;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Entity.Payment;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Exceptions.BookingNotFoundException;
import com.alpha.FindRide.Exceptions.DriverNotFoundException;
import com.alpha.FindRide.Exceptions.InvalidOtpException;
import com.alpha.FindRide.Exceptions.LocationFetchException;
import com.alpha.FindRide.Exceptions.NoCurrentBookingException;
import com.alpha.FindRide.Exceptions.VehicleNotFoundException;
import com.alpha.FindRide.Repository.AppUserRepo;
import com.alpha.FindRide.Repository.BookingRepo;
import com.alpha.FindRide.Repository.CustomerRepo;
import com.alpha.FindRide.Repository.DriverRepo;
import com.alpha.FindRide.Repository.PaymentRepo;
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
	
	@Autowired
	private BookingRepo br;
	
	@Autowired
	private CustomerRepo cr;
	
	@Autowired
	private PaymentRepo pr;

	@Autowired
	private AppUserRepo ar;
	
	public ResponseEntity<ResponseStructure<Driver>> saveDriver(RegisterDriverVehicleDTO rdto) {
		
		AppUser a = new AppUser();
		a.setMobileno(rdto.getMobileno());
		a.setPassword(rdto.getPassword());
		a.setRole("DRIVER");
		ar.save(a);
		
		Driver d = new Driver();
		d.setLicenseNo(rdto.getLicenseNo());
		d.setUpiid(rdto.getUpiid());
		d.setName(rdto.getDriverName());
		d.setAge(rdto.getAge());
		d.setMobileno(rdto.getMobileno());
		d.setGender(rdto.getGender());
		d.setMailid(rdto.getMailid());
		d.setUser(a);
		dr.save(d);
		
		Vehicle v = new Vehicle();
		
		v.setDriver(d);
		v.setName(rdto.getVehicleName());
		v.setVehicleNo(rdto.getVehicleNo());
		v.setType(rdto.getVehicleType());
		v.setModel(rdto.getModel());
		v.setCapacity(rdto.getVehicleCapacity());
				
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
		return new ResponseEntity<ResponseStructure<Driver>>(rs,HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<Driver>> findDriver(long mobileno) {
	    Driver d = dr.findByMobileno(mobileno).orElseThrow(()->new DriverNotFoundException());
        ResponseStructure<Driver> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.FOUND.value());
        rs.setMessage("Driver with MobileNo " + mobileno + " found");
        rs.setData(d);

        return new ResponseEntity<ResponseStructure<Driver>>(rs,HttpStatus.FOUND);
	}

	public ResponseEntity<ResponseStructure<Driver>> updateLocation(UpdateLocationDTO udto) {

		    Driver d = dr.findByMobileno(udto.getMobileno()).orElseThrow(()->new DriverNotFoundException());
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
		        ObjectMapper mapper = new ObjectMapper();
		        
		        JsonNode json = mapper.readTree(restTemplate.getForObject(url, String.class));

		        String city = json.get("address").get("city").asString();
		        v.setCurrentCity(city);

		    } catch (Exception e) {
		        throw new LocationFetchException("Failed to fetch location from LocationIQ: " + e.getMessage());
		    }

		    vr.save(v);
		    d.setVehicle(v);

		    ResponseStructure<Driver> rs = new ResponseStructure<>();
		    rs.setStatuscode(HttpStatus.OK.value());
		    rs.setMessage("Driver location updated successfully");
		    rs.setData(d);
		    return new ResponseEntity<ResponseStructure<Driver>>(rs,HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<String>> deleteDriver(long mobileno) {
		Driver d = dr.findByMobileno(mobileno).orElseThrow(()->new DriverNotFoundException());
		dr.delete(d);
		ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Driver deleted");
	    rs.setData("Driver with MobileNo " + mobileno + " removed");

	    return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory(long mobileno) {
		Driver d = dr.findByMobileno(mobileno).orElseThrow(()->new DriverNotFoundException());
		List<Booking> blist = d.getBookingList();
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
	
	public ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>> seeActiveBooking(long mobileno) {
		Driver d = dr.findByMobileno(mobileno).orElseThrow(()->new DriverNotFoundException());
	    Vehicle v = vr.findById(d.getId()).orElseThrow(()->new VehicleNotFoundException());
	    Booking b = vr.findActiveBookingOfDriver(mobileno).orElseThrow(()->new NoCurrentBookingException());
	    ActiveBookingDriverDTO abddto = new ActiveBookingDriverDTO();
	    abddto.setBooking(b);
	    abddto.setDrivername(d.getName());
	    abddto.setDrivermobno(mobileno);
	    abddto.setCurrentlocation(v.getCurrentCity());
	    
	    ResponseStructure<ActiveBookingDriverDTO> rs = new ResponseStructure<ActiveBookingDriverDTO>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Current Booking is Fetched");
		rs.setData(abddto);
		return new ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PaymentDTO>> completePayment(int bookingid,String paytype, int otp) {
		Booking b = br.findById(bookingid).orElseThrow(()->new BookingNotFoundException());
		if(b.getOtp()!=otp) throw new InvalidOtpException();
		b.setBookingStatus("COMPLETED");
		b.setPaymentStatus("PAID");
		
		Customer c = b.getCust();
		c.setBookingStatus(false);
		c.setCurrentloc(b.getDestinationLoc());
		
		if(c.getPenaltyCount() > 0) {
		    c.setPenaltyCount(0);
		}
		Vehicle v = b.getVehicle();
		v.setAvailableStatus("Available");
		v.setCurrentCity(b.getDestinationLoc());
		Payment p = new Payment();
		p.setVehicle(v);
		p.setCustomer(c);
		p.setBooking(b);
		p.setAmount(b.getFare());
		p.setPaymentType(paytype);
		pr.save(p);
		b.setPayment(p);
		cr.save(c);
		vr.save(v);
		br.save(b);
		PaymentDTO pdto = new PaymentDTO();
		pdto.setBooking(b);
		pdto.setCustomer(c);
		pdto.setPayment(p);
		pdto.setVehicle(v);
		ResponseStructure<PaymentDTO> rs = new ResponseStructure<PaymentDTO>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Payment Done using "+paytype);
		rs.setData(pdto);
		return new ResponseEntity<ResponseStructure<PaymentDTO>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<upiPaymentDTO>> paymentService(int bookingid, String paytype) {

	    Booking b = br.findById(bookingid).orElseThrow(() -> new BookingNotFoundException());
	    String upiid = b.getDriver().getUpiid();
	    double amount = b.getFare();
	    String upiData = "upi://pay?pa=" + upiid +"&pn=FindRide" +"&am=" + amount +"&cu=INR";

	    String encodedUpiData = URLEncoder.encode(upiData, StandardCharsets.UTF_8);

	    String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encodedUpiData;

	    RestTemplate rt = new RestTemplate();
	    byte[] qrBytes = rt.getForObject(qrUrl, byte[].class);
	    upiPaymentDTO updto = new upiPaymentDTO();
	    updto.setFare(amount);
	    updto.setQr(qrBytes);

	    ResponseStructure<upiPaymentDTO> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("UPI QR generated successfully");
	    rs.setData(updto);

	    return new ResponseEntity<ResponseStructure<upiPaymentDTO>>(rs, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PaymentDTO>> confrimPaymentCollection(int bookingid, String paytype,int otp) {
		
		return completePayment(bookingid, paytype,otp);

	}

	public ResponseEntity<ResponseStructure<Booking>> cancelbooking(int driverid, int bookingid) {
		int cancelcount=0;
		List<Booking> blist = br.findByDriverIdAndBookingDate(driverid,LocalDate.now());
		Booking book = br.findById(bookingid).orElseThrow(()->new BookingNotFoundException());
		for(Booking b:blist)
		{
			if(b.getBookingStatus()=="CANCELLED By DRIVER")
			{
				cancelcount++;
			}
		}
		Driver d = dr.findById(driverid).orElseThrow(()->new DriverNotFoundException());
		if(cancelcount>=4)
		{
			d.setStatus("BLOCKED");
			book.setBookingStatus("CANCELLED");
		}
		else if(cancelcount<4)
		{
			book.setBookingStatus("CANCELLED");
		}
		dr.save(d);
		br.save(book);
		ResponseStructure<Booking> rs = new ResponseStructure<Booking>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("Booking cancelled successfully");
		rs.setData(book);
		return new ResponseEntity<ResponseStructure<Booking>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> startride(int otp,int bookingid) 
	{	
		Booking b = br.findById(bookingid).orElseThrow(()->new BookingNotFoundException());
		if(otp!=b.getOtp()) throw new InvalidOtpException();
		int otp1 = (int)(Math.random() * 9000) + 1000;
		b.setOtp(otp1);
		ResponseStructure<String> rs = new ResponseStructure<String>();
		rs.setStatuscode(HttpStatus.OK.value());
		rs.setMessage("OTP verified successfully");
		rs.setData("OTP verified successfully");
		return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}

}
