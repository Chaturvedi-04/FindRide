package com.alpha.FindRide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Booking;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer>  {

}
