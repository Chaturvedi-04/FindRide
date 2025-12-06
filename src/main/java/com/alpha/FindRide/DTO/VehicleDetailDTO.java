package com.alpha.FindRide.DTO;

import com.alpha.FindRide.Entity.Vehicle;

public class VehicleDetailDTO {
	
	private Vehicle v;
	private double fare;
	private double estimatedtime;
	public Vehicle getV() {
		return v;
	}
	public void setV(Vehicle v) {
		this.v = v;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public double getEstimatedtime() {
		return estimatedtime;
	}
	public void setEstimatedtime(double estimatedtime) {
		this.estimatedtime = estimatedtime;
	}
	public VehicleDetailDTO(Vehicle v, double fare, double estimatedtime) {
		super();
		this.v = v;
		this.fare = fare;
		this.estimatedtime = estimatedtime;
	}
	public VehicleDetailDTO() {
		super();
	}
	@Override
	public String toString() {
		return "VehicleDetailDTO [v=" + v + ", fare=" + fare + ", estimatedtime=" + estimatedtime + "]";
	}
}
