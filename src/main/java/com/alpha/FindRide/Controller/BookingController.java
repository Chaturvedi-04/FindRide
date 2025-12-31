package com.alpha.FindRide.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.DTO.ActiveBookingDTO;
import com.alpha.FindRide.DTO.BookingDTO;
import com.alpha.FindRide.Service.BookingService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/customer")
public class BookingController {

    @Autowired
    private BookingService bs;

    private long getMobileFromToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }

    @PostMapping("/bookVehicle")
    public ResponseEntity<ResponseStructure<ActiveBookingDTO>> bookVehicle(@RequestBody BookingDTO bookingdto) {
        return bs.bookVehicle(getMobileFromToken(), bookingdto);
    }
}


