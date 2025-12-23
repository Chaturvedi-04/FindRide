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
import com.alpha.FindRide.Exceptions.CustomerNotFoundException;
import com.alpha.FindRide.Exceptions.DriverNotFoundException;
import com.alpha.FindRide.Exceptions.VehicleNotFoundException;
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
	
	@Autowired
	private MailService ms;
	
	public ResponseEntity<ResponseStructure<ActiveBookingDTO>> bookVehicle(long mobileno,BookingDTO bookingdto)
	{
		Customer c = cr.findByMobileno(mobileno).orElseThrow(()->new CustomerNotFoundException());
		Booking b1 = new Booking();
		if(c.isBookingStatus()==true)
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
			Vehicle v = vr.findById(bookingdto.getVehicleid()).orElseThrow(()->new VehicleNotFoundException());
			Driver d = dr.findById(v.getId()).orElseThrow(()->new DriverNotFoundException());
			Booking b = new Booking();
			b.setCust(c);
			b.setVehicle(v);
			b.setDriver(d);
			b.setPaymentStatus("NOT PAID");
			b.setSourceLoc(bookingdto.getSourceLoc());
			b.setDestinationLoc(bookingdto.getDestinationLoc());
			b.setFare(bookingdto.getFare());
			b.setEstimatedTime(bookingdto.getEstimatedTime());
			b.setDistanceTravelled(bookingdto.getDistanceTravelled());
			b.setBookingDate(LocalDate.now());
			b.setBookingStatus("BOOKED");
			int otp = (int)(Math.random() * 9000) + 1000;
			b.setOtp(otp);
		    double baseFare = v.getPricePerKM() * bookingdto.getDistanceTravelled();
		    double totalFare = baseFare;
		    int penaltyCount = c.getPenaltyCount();
		    if (penaltyCount > 1) {
		        totalFare = baseFare + (baseFare / 100) * penaltyCount * 10;
		    }
		    b.setFare(totalFare);
			br.save(b);
			
			c.getBookingList().add(b);
			c.setBookingStatus(true);
			v.getDriver().getBookingList().add(b);
			v.setAvailableStatus("BOOKED");
			cr.save(c);
			vr.save(v);
			
			ActiveBookingDTO abdto = new ActiveBookingDTO();
			abdto.setBooking(b);
			abdto.setCustname(c.getName());
			abdto.setCustmobno(mobileno);
			abdto.setCurrentlocation(c.getCurrentloc());
			ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<ActiveBookingDTO>();
			rs.setStatuscode(HttpStatus.OK.value());
			rs.setMessage("You have successfully booked the vehicle");
			rs.setData(abdto);
			ms.sendMail(c.getEmailid(),"Booking Confirmation","Dear" +c.getName()+"\r\n"
					+ "\r\n"
					+ "Your booking has been successfully confirmed! 🎉\r\n"
					+ "\r\n"
					+ "📌 Booking Details:\r\n"
					+ "-----------------------------------\r\n"
					+ "Booking ID     : "+b.getId()+"\r\n"
					+ "Date           : "+b.getBookingDate()+"\r\n"
					+ "Pickup Location: "+c.getCurrentloc()+"\r\n"
					+ "Drop Location  : "+b.getDestinationLoc()+"\r\n"
					+ "Amount To Be Paid    : "+b.getFare()+"\r\n"
					+ "-----------------------------------\r\n"
					+ "\r\n"
					+ "Our driver "+d.getName()+" will contact you shortly before pickup.\r\n"
					+ "\r\n"
					+ "If you have any questions or need assistance, feel free to contact us.\r\n"
					+ "\r\n"
					+ "Thank you for choosing findRide.\r\n"
					+ "We wish you a safe and pleasant journey!\r\n"
					+ "\r\n"
					+ "Best Regards,  \r\n"
					+ "findRide Support Team  \r\n"
					+ "📞 9908223304  \r\n"
					+ "📧 support@gmail.com\r\n"
					+ "");
			ms.sendMail(d.getMailid(),"Booking Confirmation","\"Booking Assigned\",\"Dear" + d.getName() + "\r\n"
					+ "        + \"You have been assigned a new booking! 🚗\\r\\n\"\r\n"
					+ "        + \"\\r\\n\"\r\n"
					+ "        + \"📌 Booking Details:\\r\\n\"\r\n"
					+ "        + \"-----------------------------------\\r\\n\"\r\n"
					+ "        + \"Booking ID     : " + b.getId() + "\\r\\n\"\r\n"
					+ "        + \"Date           : " + b.getBookingDate() + "\\r\\n\"\r\n"
					+ "        + \"Customer Name  : " + c.getName() + "\\r\\n\"\r\n"
					+ "        + \"Pickup Location: " + c.getCurrentloc() + "\\r\\n\"\r\n"
					+ "        + \"Drop Location  : " + b.getDestinationLoc() + "\\r\\n\"\r\n"
					+ "        + \"Fare Amount    : " + b.getFare() + "\\r\\n\"\r\n"
					+ "        + \"-----------------------------------\\r\\n\"\r\n"
					+ "        + \"\\r\\n\"\r\n"
					+ "        + \"Please ensure timely arrival at the pickup location.\\r\\n\"\r\n"
					+ "        + \"Contact the customer before starting the trip if required.\\r\\n\"\r\n"
					+ "        + \"\\r\\n\"\r\n"
					+ "        + \"We wish you a safe and successful ride.\\r\\n\"\r\n"
					+ "        + \"\\r\\n\"\r\n"
					+ "        + \"Best Regards,  \\r\\n\"\r\n"
					+ "        + \"findRide Driver Support Team  \\r\\n\"\r\n"
					+ "        + \"📞 9908223304  \\r\\n\"\r\n"
					+ "        + \"📧 support@gmail.com\\r\\n\"\r\n"
					+ "        + \"\"\r\n"
					+ "");
			return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs,HttpStatus.OK);
		}
	}
}
