package com.alpha.FindRide.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "cust_id")
	@JsonIgnore
	private Customer cust;
	
	@OneToOne
	@JsonIgnore
	private Driver driver;
	
	@OneToOne
	@JsonIgnore
	private Vehicle vehicle;
	
	private String sourceLoc;
	private String destinationLoc;
	private double distanceTravelled;
	private double fare;
	private double estimatedTime;
	private LocalDate bookingDate;
	private String paymentStatus;
	@OneToOne
	@JsonIgnore
	private Payment payment;
	private String bookingStatus="AVAILABLE";
	private int otp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Customer getCust() {
		return cust;
	}
	public void setCust(Customer cust) {
		this.cust = cust;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public String getSourceLoc() {
		return sourceLoc;
	}
	public void setSourceLoc(String sourceLoc) {
		this.sourceLoc = sourceLoc;
	}
	public String getDestinationLoc() {
		return destinationLoc;
	}
	public void setDestinationLoc(String destinationLoc) {
		this.destinationLoc = destinationLoc;
	}
	public double getDistanceTravelled() {
		return distanceTravelled;
	}
	public void setDistanceTravelled(double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public Booking(int id, Customer cust, Driver driver, Vehicle vehicle, String sourceLoc, String destinationLoc,
			double distanceTravelled, double fare, double estimatedTime, LocalDate bookingDate, String paymentStatus,
			Payment payment, String bookingStatus, int otp) {
		super();
		this.id = id;
		this.cust = cust;
		this.driver = driver;
		this.vehicle = vehicle;
		this.sourceLoc = sourceLoc;
		this.destinationLoc = destinationLoc;
		this.distanceTravelled = distanceTravelled;
		this.fare = fare;
		this.estimatedTime = estimatedTime;
		this.bookingDate = bookingDate;
		this.paymentStatus = paymentStatus;
		this.payment = payment;
		this.bookingStatus = bookingStatus;
		this.otp = otp;
	}
	public Booking() {
		super();
	}
	@Override
	public String toString() {
		return "Booking [id=" + id + ", cust=" + cust + ", driver=" + driver + ", vehicle=" + vehicle + ", sourceLoc="
				+ sourceLoc + ", destinationLoc=" + destinationLoc + ", distanceTravelled=" + distanceTravelled
				+ ", fare=" + fare + ", estimatedTime=" + estimatedTime + ", bookingDate=" + bookingDate
				+ ", paymentStatus=" + paymentStatus + ", payment=" + payment + ", bookingStatus=" + bookingStatus
				+ ", otp=" + otp + "]";
	}
}