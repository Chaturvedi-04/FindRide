package com.alpha.FindRide.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDTO;
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
	
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> bookVehicle(long mobileno,BookingDTO bookingdto)
	{
		Customer c = cr.findByMobileno(mobileno);
		Booking b1 = new Booking();
		if(c.getBookingStatus()==true)
		{
			List<Booking> blist = c.getBookingList();
			for(Booking b: blist)
			{
				if ("BOOKED".equals(b.getBookingStatus()))
				{
					b1=b;
					break;
				}
			}
			ActiveBookingDTO abdto = new ActiveBookingDTO();
			abdto.setBooking(b1);
			abdto.setCustname(c.getName());
			abdto.setCustmobno(mobileno);
			abdto.setCurrentlocation(c.getCurrentloc());
			ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<ActiveBookingDTO>();
			rs.setStatuscode(HttpStatus.OK.value());
			rs.setMessage("You have alredy booked one vehicle cant book one more vehicle");
			rs.setData(abdto);
			return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs,HttpStatus.OK);
		}
		else
		{
			Vehicle v = vr.findById(bookingdto.getVehicleid()).get();
			Driver d = dr.findById(v.getId()).get();
			Booking b = new Booking();
			b.setCust(c);
			b.setVehicle(v);
			b.setDriver(d);
			b.setSourceLoc(bookingdto.getSourceLoc());
			b.setDestinationLoc(bookingdto.getDestinationLoc());
			b.setFare(bookingdto.getFare());
			b.setEstimatedTime(bookingdto.getEstimatedTime());
			b.setDistanceTravelled(bookingdto.getDistanceTravelled());
			b.setBookingDate(LocalDate.now().toString());
			b.setBookingStatus("BOOKED");
			br.save(b);
			
			c.getBookingList().add(b);
			c.setBookingStatus(true);
			v.getDriver().getBookingList().add(b);
			v.setAvailableStatus("BOOKED");
			d.setStatus("BOOKED");
			cr.save(c);
			vr.save(v);
			dr.save(d);
			
			ActiveBookingDTO abdto = new ActiveBookingDTO();
			abdto.setBooking(b);
			abdto.setCustname(c.getName());
			abdto.setCustmobno(mobileno);
			abdto.setCurrentlocation(c.getCurrentloc());
			ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<ActiveBookingDTO>();
			rs.setStatuscode(HttpStatus.OK.value());
			rs.setMessage("You have successfully booked the vehicle");
			rs.setData(abdto);
			return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs,HttpStatus.OK);
		}
	}
}
