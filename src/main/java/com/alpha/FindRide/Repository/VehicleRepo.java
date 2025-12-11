package com.alpha.FindRide.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Vehicle;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer>{

	 @Query("SELECT v FROM Vehicle v " + "WHERE v.currentCity = :sourceLoc AND v.availableStatus = 'Available'")
	public List<Vehicle> findByCurrentCity(String sourceLoc);
	 
	 @Query("SELECT b FROM Booking b WHERE b.cust.mobileno = :mobileno AND b.bookingStatus = 'COMPLETED'")
	 public List<Booking> findAllCompletedBookingsOfCustomer(long mobileno);
	 
	 @Query("SELECT b FROM Booking b WHERE b.driver.mobileno = :mobileno AND b.bookingStatus = 'COMPLETED'")
	 public List<Booking> findAllCompletedBookingsOfDriver(long mobileno);
	 
	 @Query("SELECT b FROM Booking b WHERE b.cust.mobileno = :mobileno AND b.bookingStatus = 'BOOKED'")
	 public Booking findActiveBookingOfCustomer(long mobileno);
	 
	 @Query("SELECT b FROM Booking b WHERE b.driver.mobileno = :mobileno AND b.bookingStatus = 'BOOKED'")
	 public Booking findActiveBookingOfDriver(long mobileno);
	
}
