package com.alpha.FindRide.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Payment {

	@Id
	private int id;
	@OneToOne
	@JsonIgnore
	private Customer customer;
	@OneToOne
	@JsonIgnore
	private Vehicle vehicle;
	@OneToOne
	@JsonIgnore
	private Booking booking;
	private double amount;
	private String paymenType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymenType() {
		return paymenType;
	}
	public void setPaymenType(String paymenType) {
		this.paymenType = paymenType;
	}
	public Payment(int id, Customer customer, Vehicle vehicle, Booking booking, double amount, String paymenType) {
		super();
		this.id = id;
		this.customer = customer;
		this.vehicle = vehicle;
		this.booking = booking;
		this.amount = amount;
		this.paymenType = paymenType;
	}
	public Payment() {
		super();
	}
	@Override
	public String toString() {
		return "Payment [id=" + id + ", customer=" + customer + ", vehicle=" + vehicle + ", booking=" + booking
				+ ", amount=" + amount + ", paymenType=" + paymenType + "]";
	}
	
}