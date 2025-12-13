package com.alpha.FindRide.DTO;

public class RidedetailDTO {
	
	private String fromloc;
	private String toloc;
	private double distance;
	private double fare;
	public String getFromloc() {
		return fromloc;
	}
	public void setFromloc(String fromloc) {
		this.fromloc = fromloc;
	}
	public String getToloc() {
		return toloc;
	}
	public void setToloc(String toloc) {
		this.toloc = toloc;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public RidedetailDTO(String fromloc, String toloc, double distance, double fare) {
		super();
		this.fromloc = fromloc;
		this.toloc = toloc;
		this.distance = distance;
		this.fare = fare;
	}
	public RidedetailDTO() {
		super();
	}
	@Override
	public String toString() {
		return "RidedetailDTO [fromloc=" + fromloc + ", toloc=" + toloc + ", distance=" + distance + ", fare=" + fare
				+ "]";
	}
}
