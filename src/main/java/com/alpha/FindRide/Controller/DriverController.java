package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDriverDTO;
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.PaymentDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.DTO.upiPaymentDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Service.DriverService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService ds;

    // 🔐 Extract mobile from JWT
    private long getMobileFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }

    @PostMapping("/saveDriver")
    public ResponseEntity<ResponseStructure<Driver>> saveDriver(@RequestBody RegisterDriverVehicleDTO rdto) {
        return ds.saveDriver(rdto);
    }

    @GetMapping("/findDriver")
    public ResponseEntity<ResponseStructure<Driver>> findDriver() {
        return ds.findDriver(getMobileFromToken());
    }

    @PutMapping("/updateLocation")
    public ResponseEntity<ResponseStructure<Driver>> updateLocation(@RequestBody UpdateLocationDTO udto) {
        return ds.updateLocation(getMobileFromToken(), udto);
    }

    @DeleteMapping("/deleteDriver")
    public ResponseEntity<ResponseStructure<String>> deleteDriver() {
        return ds.deleteDriver(getMobileFromToken());
    }

    @GetMapping("/seeBookingHistory")
    public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory() {
        return ds.seeBookingHistory(getMobileFromToken());
    }

    @GetMapping("/seeActiveBooking")
    public ResponseEntity<ResponseStructure<ActiveBookingDriverDTO>> seeActiveBooking() {
        return ds.seeActiveBooking(getMobileFromToken());
    }

    @PostMapping("/completeride/payByCash")
    public ResponseEntity<ResponseStructure<PaymentDTO>> completePayment(@RequestParam int bookingid,@RequestParam String paytype,@RequestParam int otp) {
        return ds.completePayment(getMobileFromToken(), bookingid, paytype, otp);
    }

    @PostMapping("/completeride/payByUpi")
    public ResponseEntity<ResponseStructure<upiPaymentDTO>> paymentService(@RequestParam int bookingid,@RequestParam String paytype) {
        return ds.paymentService(bookingid, paytype);
    }

    @PostMapping("/ridecompleted/paymentconfirmed")
    public ResponseEntity<ResponseStructure<PaymentDTO>> confrimPaymentCollection(@RequestParam int bookingid,@RequestParam String paytype,@RequestParam int otp) {
        return ds.confrimPaymentCollection(getMobileFromToken(), bookingid, paytype, otp);
    }

    @PostMapping("/cancelbooking")
    public ResponseEntity<ResponseStructure<Booking>> cancelbooking(@RequestParam int bookingid) {
        return ds.cancelbooking(getMobileFromToken(), bookingid);
    }

    @PostMapping("/startride")
    public ResponseEntity<ResponseStructure<String>> startride( @RequestParam int otp,@RequestParam int bookingid) {
        return ds.startride(getMobileFromToken(), otp, bookingid);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<ResponseStructure<Driver>> updateDriverStatus( @RequestParam boolean active) {
        return ds.updateDriverStatus(getMobileFromToken(), active);
    }
}
