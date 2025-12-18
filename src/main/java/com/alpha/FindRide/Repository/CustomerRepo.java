package com.alpha.FindRide.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByMobileno(long mobileno);

}
