package com.alpha.FindRide.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.FindRide.Repository.CustomerRepo;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepo cr;
	

}
