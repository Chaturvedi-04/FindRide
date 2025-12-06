package com.alpha.FindRide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Driver;

@Repository
public interface DriverRepo extends JpaRepository<Driver ,Integer>{


    Driver findByMobileno(long mobileno);
}
