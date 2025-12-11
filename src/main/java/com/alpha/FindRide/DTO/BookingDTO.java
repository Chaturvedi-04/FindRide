package com.alpha.FindRide.DTO;

public class BookingDTO {
	
	private int vehicleid;
	private String sourceLoc;
	private String destinationLoc;
	private double distanceTravelled;
	private double fare;
	private double estimatedTime;
	public int getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(int vehicleid) {
		this.vehicleid = vehicleid;
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
	public BookingDTO(int vehicleid, String sourceLoc, String destinationLoc, double distanceTravelled, double fare,
			double estimatedTime) {
		super();
		this.vehicleid = vehicleid;
		this.sourceLoc = sourceLoc;
		this.destinationLoc = destinationLoc;
		this.distanceTravelled = distanceTravelled;
		this.fare = fare;
		this.estimatedTime = estimatedTime;
	}
	public BookingDTO() {
		super();
	}
	@Override
	public String toString() {
		return "BookingDTO [vehicleid=" + vehicleid + ", sourceLoc=" + sourceLoc + ", destinationLoc=" + destinationLoc
				+ ", distanceTravelled=" + distanceTravelled + ", fare=" + fare + ", estimatedTime=" + estimatedTime
				+ "]";
	}
}
