package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.AvailableVehicleDTO;
import com.alpha.FindRide.DTO.BookingHistoryDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.DTO.UpdateLocationDTO;
import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Service.CustomerService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService cs;

    private long getMobileFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }

    @PostMapping("/saveCustomer")
    public ResponseEntity<ResponseStructure<Customer>> saveCustomer(@RequestBody RegisterCustomerDTO rdto) {
        return cs.saveCustomer(rdto);
    }

    @GetMapping("/findCustomer")
    public ResponseEntity<ResponseStructure<Customer>> findCustomer() {
        return cs.findCustomer(getMobileFromToken());
    }

    @DeleteMapping("/deleteCustomer")
    public ResponseEntity<ResponseStructure<String>> deleteCustomer() {
        return cs.deleteCustomer(getMobileFromToken());
    }
    
    @PostMapping("/updateLocation")
    public ResponseEntity<ResponseStructure<Customer>> updateLocation(@RequestBody UpdateLocationDTO udto) {
        return cs.updateCustomerLocation(getMobileFromToken(), udto);
    }

    @GetMapping("/seeallAvailableVehicles")
    public ResponseEntity<ResponseStructure<AvailableVehicleDTO>> seeallAvailableVehicles(@RequestParam String destinationCity) {
        return cs.seeallAvailableVehicles(getMobileFromToken(), destinationCity);
    }

    @GetMapping("/seeBookingHistory")
    public ResponseEntity<ResponseStructure<BookingHistoryDTO>> seeBookingHistory() {
        return cs.seeBookingHistory(getMobileFromToken());
    }

    @GetMapping("/seeActiveBooking")
    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> seeActiveBooking() {
        return cs.seeActiveBooking(getMobileFromToken());
    }

    @PostMapping("/cancelbooking")
    public ResponseEntity<ResponseStructure<Booking>> cancelBooking(@RequestParam int bookingid) {
        return cs.cancelbooking(getMobileFromToken(), bookingid);
    }

}
