package com.alpha.FindRide.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer>{

	List<Booking> findByDriverIdAndBookingDate(int driverid, LocalDate now);

}
