package com.alpha.FindRide.Service;

import java.time.LocalDate;

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

import jakarta.transaction.Transactional;

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

    @Transactional
    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> bookVehicle(long mobileno, BookingDTO bookingdto) {

        Customer c = cr.findByMobileno(mobileno).orElseThrow(()-> new CustomerNotFoundException());

        if (c.isBookingStatus()) {

            Booking activeBooking = null;
            for (Booking b : c.getBookingList()) {
                if ("BOOKED".equals(b.getBookingStatus())) {
                    activeBooking = b;
                    break;
                }
            }

            ActiveBookingDTO abdto = new ActiveBookingDTO();
            abdto.setBooking(activeBooking);
            abdto.setCustname(c.getName());
            abdto.setCustmobno(mobileno);
            abdto.setCurrentlocation(c.getCurrentloc());

            ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
            rs.setStatuscode(HttpStatus.OK.value());
            rs.setMessage("You have already booked one vehicle");
            rs.setData(abdto);

            return new ResponseEntity<ResponseStructure<ActiveBookingDTO>>(rs, HttpStatus.OK);
        }

        Vehicle v = vr.findById(bookingdto.getVehicleid())
                .orElseThrow(VehicleNotFoundException::new);

        Driver d = v.getDriver();
        if (d == null) throw new DriverNotFoundException();

        Booking b = new Booking();
        b.setCust(c);
        b.setDriver(d);
        b.setVehicle(v);
        b.setPaymentStatus("NOT_PAID");
        b.setSourceLoc(bookingdto.getSourceLoc());
        b.setDestinationLoc(bookingdto.getDestinationLoc());
        b.setEstimatedTime(bookingdto.getEstimatedTime());
        b.setDistanceTravelled(bookingdto.getDistanceTravelled());
        b.setBookingDate(LocalDate.now());
        b.setBookingStatus("BOOKED");
        b.setOtp((int) (Math.random() * 9000) + 1000);

        double baseFare = v.getPricePerKM() * bookingdto.getDistanceTravelled();
        if (c.getPenaltyCount() > 1) {
            baseFare += (baseFare / 100) * c.getPenaltyCount() * 10;
        }
        b.setFare(baseFare);

        c.getBookingList().add(b);
        d.getBookingList().add(b);
        c.setBookingStatus(true);
        v.setAvailableStatus("BOOKED");

        br.save(b);
        cr.save(c);
        dr.save(d);
        vr.save(v);

        ActiveBookingDTO abdto = new ActiveBookingDTO();
        abdto.setBooking(b);
        abdto.setCustname(c.getName());
        abdto.setCustmobno(mobileno);
        abdto.setCurrentlocation(c.getCurrentloc());

        ResponseStructure<ActiveBookingDTO> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("You have successfully booked the vehicle");
        rs.setData(abdto);        
		ms.sendMail(c.getEmailid(),"Booking Confirmation",
					"Dear" +c.getName()+"\r\n"
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
