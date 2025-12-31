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
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<ResponseStructure<Driver>> saveDriver(RegisterDriverVehicleDTO rdto) {
		
		AppUser a = new AppUser();
		a.setMobileno(rdto.getMobileno());
		a.setPassword(passwordEncoder.encode(rdto.getPassword()));
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
		v.setAvailableStatus("AVAILABLE");
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
	
	public ResponseEntity<ResponseStructure<Driver>> findDriver(long mobile) {

	    Driver d = dr.findByMobileno(mobile).orElseThrow(DriverNotFoundException::new);

	    ResponseStructure<Driver> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Driver fetched");
	    rs.setData(d);

	    return ResponseEntity.ok(rs);
	}


	public ResponseEntity<ResponseStructure<Driver>> updateLocation(long mobile,UpdateLocationDTO udto) {

	    Driver d = dr.findByMobileno(mobile).orElseThrow(DriverNotFoundException::new);

	    Vehicle v = d.getVehicle();
	    
	    if (v == null) throw new VehicleNotFoundException();

	    try {
	        String url = "https://us1.locationiq.com/v1/reverse?key=" + apiKey +
	                "&lat=" + udto.getLatitude() +
	                "&lon=" + udto.getLongitude() +
	                "&format=json";

	        RestTemplate rt = new RestTemplate();
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode json = mapper.readTree(rt.getForObject(url, String.class));

	        String city = json.get("address").get("city").asText();
	        v.setCurrentCity(city);

	    } 
	    catch (Exception e) {
	        throw new LocationFetchException("Location fetch failed");
	    }

	    vr.save(v);

	    ResponseStructure<Driver> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Location updated");
	    rs.setData(d);

	    return ResponseEntity.ok(rs);
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

	public ResponseEntity<ResponseStructure<PaymentDTO>> completePayment(long driverMobile,int bookingId,String paytype,int otp) {

	    Booking b = br.findById(bookingId).orElseThrow(()-> new BookingNotFoundException());

	    if (otp != b.getOtp()) {
	        throw new InvalidOtpException();
	    }

	    b.setBookingStatus("COMPLETED");
	    b.setPaymentStatus("PAID");

	    Customer c = b.getCust();
	    c.setBookingStatus(false);
	    c.setCurrentloc(b.getDestinationLoc());
	    c.setPenaltyCount(0);

	    Vehicle v = b.getVehicle();
	    v.setAvailableStatus("AVAILABLE");
	    v.setCurrentCity(b.getDestinationLoc());

	    
	    Payment p = new Payment();
	    p.setBooking(b);
	    p.setCustomer(c);
	    p.setVehicle(v);
	    p.setAmount(b.getFare());
	    p.setPaymentType(paytype);

	    pr.save(p);

	    b.setPayment(p);
	    br.save(b);
	    cr.save(c);
	    vr.save(v);

	    PaymentDTO pdto = new PaymentDTO();
	    pdto.setBooking(b);
	    pdto.setCustomer(c);
	    pdto.setVehicle(v);
	    pdto.setPayment(p);

	    ResponseStructure<PaymentDTO> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Payment completed using " + paytype);
	    rs.setData(pdto);

	    return new ResponseEntity<ResponseStructure<PaymentDTO>>(rs,HttpStatus.OK);
	}
	
	public ResponseEntity<ResponseStructure<upiPaymentDTO>> paymentService(int bookingId,String paytype) {

	    Booking b = br.findById(bookingId).orElseThrow(()-> new BookingNotFoundException());

	    String upiId = b.getDriver().getUpiid();
	    double amount = b.getFare();

	    String upiData = "upi://pay?pa=" + upiId +
	            "&pn=FindRide&am=" + amount +
	            "&cu=INR";

	    String encoded = URLEncoder.encode(upiData, StandardCharsets.UTF_8);
	    String qrUrl =
	            "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + encoded;

	    RestTemplate rt = new RestTemplate();
	    byte[] qrBytes = rt.getForObject(qrUrl, byte[].class);

	    upiPaymentDTO dto = new upiPaymentDTO();
	    dto.setFare(amount);
	    dto.setQr(qrBytes);

	    ResponseStructure<upiPaymentDTO> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("UPI QR generated");
	    rs.setData(dto);

	    return new ResponseEntity<ResponseStructure<upiPaymentDTO>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<PaymentDTO>> confrimPaymentCollection (long driverMobile, int bookingId, String paytype,int otp) {

	    return completePayment(driverMobile, bookingId, paytype, otp);
	}


	public ResponseEntity<ResponseStructure<Booking>> cancelbooking(long mobile,int bookingId) {

	    Driver d = dr.findByMobileno(mobile).orElseThrow(()-> new DriverNotFoundException());

	    Booking b = br.findById(bookingId).orElseThrow(()-> new BookingNotFoundException());

	    int cancelCount = 0;

	    List<Booking> todayBookings = br.findByDriverIdAndBookingDate(d.getId(), LocalDate.now());

	    for (Booking bk : todayBookings) {
	        if ("CANCELLED_BY_DRIVER".equals(bk.getBookingStatus())) {
	            cancelCount++;
	        }
	    }

	    b.setBookingStatus("CANCELLED_BY_DRIVER");

	    if (cancelCount >= 4) {
	        d.setStatus("BLOCKED");
	        dr.save(d);
	    }

	    br.save(b);

	    ResponseStructure<Booking> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Booking cancelled");
	    rs.setData(b);

	    return new ResponseEntity<ResponseStructure<Booking>>(rs,HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<String>> startride(long mobile, int otp, int bookingId) {

	    Booking b = br.findById(bookingId).orElseThrow(()-> new BookingNotFoundException());

	    if (otp != b.getOtp()) throw new InvalidOtpException();

	    int newOtp = (int) (Math.random() * 9000) + 1000;
	    b.setOtp(newOtp);
	    br.save(b);

	    ResponseStructure<String> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Ride started");
	    rs.setData("OTP verified");

	    return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}


	public ResponseEntity<ResponseStructure<Driver>> updateDriverStatus(long mobile,boolean active) {

	    Driver d = dr.findByMobileno(mobile).orElseThrow(()-> new DriverNotFoundException());

	    Vehicle v = d.getVehicle();
	    if (v == null) throw new VehicleNotFoundException();

	    if (active) {
	        d.setStatus("AVAILABLE");
	        v.setAvailableStatus("AVAILABLE");
	    } 
	    else {
	        d.setStatus("NOT_AVAILABLE");
	        v.setAvailableStatus("NOT_AVAILABLE");
	    }

	    vr.save(v);
	    dr.save(d);

	    ResponseStructure<Driver> rs = new ResponseStructure<>();
	    rs.setStatuscode(HttpStatus.OK.value());
	    rs.setMessage("Status updated");
	    rs.setData(d);

	    return new ResponseEntity<ResponseStructure<Driver>>(rs,HttpStatus.OK);
	}

}
