package com.alpha.FindRide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

}
