package com.alpha.FindRide.DTO;

import com.alpha.FindRide.Entity.Booking;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Payment;
import com.alpha.FindRide.Entity.Vehicle;

public class PaymentDTO {
	
	private Customer customer;
	private Vehicle vehicle;
	private Booking booking;
	private Payment payment;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public PaymentDTO() {
		super();
	}
	public PaymentDTO(Customer customer, Vehicle vehicle, Booking booking, Payment payment) {
		super();
		this.customer = customer;
		this.vehicle = vehicle;
		this.booking = booking;
		this.payment = payment;
	}
	@Override
	public String toString() {
		return "CashPaymentDTO [customer=" + customer + ", vehicle=" + vehicle + ", booking=" + booking + ", payment="
				+ payment + "]";
	}
}
