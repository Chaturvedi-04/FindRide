package com.alpha.FindRide.DTO;

public class BookingDTO {

	private int vehicleId;
	private String sourceLocation;
	private String destinationLocation;
	private double fare;
	private double distanceEstimated;
	private double estimatedTime;
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getSourceLocation() {
		return sourceLocation;
	}
	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}
	public String getDestinationLocation() {
		return destinationLocation;
	}
	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getDistanceEstimated() {
		return distanceEstimated;
	}
	public void setDistanceEstimated(double distanceEstimated) {
		this.distanceEstimated = distanceEstimated;
	}
	public double getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public BookingDTO(int vehicleId, String sourceLocation, String destinationLocation, double fare,
			double distanceEstimated, double estimatedTime) {
		super();
		this.vehicleId = vehicleId;
		this.sourceLocation = sourceLocation;
		this.destinationLocation = destinationLocation;
		this.fare = fare;
		this.distanceEstimated = distanceEstimated;
		this.estimatedTime = estimatedTime;
	}
	@Override
	public String toString() {
		return "BookingDTO [vehicleId=" + vehicleId + ", sourceLocation=" + sourceLocation + ", destinationLocation="
				+ destinationLocation + ", fare=" + fare + ", distanceEstimated=" + distanceEstimated
				+ ", estimatedTime=" + estimatedTime + "]";
	}
	public BookingDTO() {
		super();
	}	
}
