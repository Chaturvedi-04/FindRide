package com.alpha.FindRide.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.BookingDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Entity.Vehicle;
import com.alpha.FindRide.Repository.BookingRepo;
import com.alpha.FindRide.Repository.CustomerRepo;
import com.alpha.FindRide.Repository.DriverRepo;
import com.alpha.FindRide.Repository.VehicleRepo;

@Service
public class BookingService {

	@Autowired 
	private BookingRepo br;
	
	@Autowired
	private CustomerRepo cr;
	
	@Autowired
	private VehicleRepo vr;
	
	@Autowired
	private DriverRepo dr;
	
	public ResponseStructure<Booking> bookVehicle(long mobileno, BookingDTO bdto)
	{
		Customer c= cr.findByMobileno(mobileno);
		Vehicle v=vr.findById(bdto.getVehicleId()).get();
		Driver d=dr.findById(v.getId()).get();
		
		Booking b= new Booking();
		b.setCust(c);
		b.setVehicle(v);
		b.setSourceLoc(bdto.getSourceLocation());
		b.setDestinationLoc(bdto.getDestinationLocation());
		b.setFare(bdto.getFare());
		b.setEstimatedTime(bdto.getEstimatedTime());
		b.setDistanceTravelled(bdto.getDistanceEstimated());
		b.setBookingDate(LocalDateTime.now().toString());
		b.setBookingStatus("BOOKED");
		br.save(b);
		
		c.getBookingList().add(b);
		v.getDriver().getBookingList().add(b);
		v.setAvailableStatus("BOOKED");
		d.setStatus("BOOKED");
		cr.save(c);
		vr.save(v);
		dr.save(d);
		ResponseStructure<Booking> rs = new ResponseStructure<Booking>();
		rs.setStatuscode(HttpStatus.ACCEPTED.value());
		rs.setMessage("Booking done succesfully");
		rs.setData(b);
		return rs;
	}
}
