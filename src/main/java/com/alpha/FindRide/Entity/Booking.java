package com.alpha.FindRide.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne
	private Customer cust;
	@OneToOne
	private Driver driver;
	private String sourceLoc;
	private String destinationLoc;
	private double distanceTravelled;
	private double fare;
	private String estimatedTime;
	private String bookingDate;
//	private Payment payment;
	public Booking(int id, Customer cust, Driver driver, String sourceLoc, String destinationLoc, double distanceTravelled,
			double fare, String estimatedTime, String bookingDate) {
		super();
		this.id = id;
		this.cust = cust;
		this.driver = driver;
		this.sourceLoc = sourceLoc;
		this.destinationLoc = destinationLoc;
		this.distanceTravelled = distanceTravelled;
		this.fare = fare;
		this.estimatedTime = estimatedTime;
		this.bookingDate = bookingDate;
	}
public Booking() {
	super();
}
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
public String getEstimatedTime() {
	return estimatedTime;
}
public void setEstimatedTime(String estimatedTime) {
	this.estimatedTime = estimatedTime;
}
public String getBookingDate() {
	return bookingDate;
}
public void setBookingDate(String bookingDate) {
	this.bookingDate = bookingDate;
}
@Override
public String toString() {
	return "Booking [id=" + id + ", cust=" + cust + ", driver=" + driver + ", sourceLoc=" + sourceLoc
			+ ", destinationLoc=" + destinationLoc + ", distanceTravelled=" + distanceTravelled + ", fare=" + fare
			+ ", estimatedTime=" + estimatedTime + ", bookingDate=" + bookingDate + "]";
}
}