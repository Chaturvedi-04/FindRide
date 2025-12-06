package com.alpha.FindRide.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Vehicle;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer>{

	 @Query("SELECT v FROM Vehicle v " +
	           "WHERE v.currentCity = :sourceLoc AND v.availableStatus = 'Available'")
	public List<Vehicle> findByCurrentCity(String sourceLoc);
	
}
