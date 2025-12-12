package com.alpha.FindRide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.FindRide.Entity.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {

}
