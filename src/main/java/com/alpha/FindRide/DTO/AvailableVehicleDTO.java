package com.alpha.FindRide.DTO;

import java.util.List;

import com.alpha.FindRide.Entity.Customer;

public class AvailableVehicleDTO {
	
	private Customer c;
	private double distance;
	private String sourceloc;
	private String destinationloc;
	private double penaltyamount;
	private List<VehicleDetailDTO> availableVehicles;
	
	public AvailableVehicleDTO(Customer c, double distance, String sourceloc, String destinationloc,
			double penaltyamount, List<VehicleDetailDTO> availableVehicles) {
		super();
		this.c = c;
		this.distance = distance;
		this.sourceloc = sourceloc;
		this.destinationloc = destinationloc;
		this.penaltyamount = penaltyamount;
		this.availableVehicles = availableVehicles;
	}
	public double getPenaltyamount() {
		return penaltyamount;
	}
	public void setPenaltyamount(double penaltyamount) {
		this.penaltyamount = penaltyamount;
	}
	public Customer getC() {
		return c;
	}
	public void setC(Customer c) {
		this.c = c;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getSourceloc() {
		return sourceloc;
	}
	public void setSourceloc(String sourceloc) {
		this.sourceloc = sourceloc;
	}
	public String getDestinationloc() {
		return destinationloc;
	}
	public void setDestinationloc(String destinationloc) {
		this.destinationloc = destinationloc;
	}
	public List<VehicleDetailDTO> getAvailableVehicles() {
		return availableVehicles;
	}
	public void setAvailableVehicles(List<VehicleDetailDTO> availableVehicles) {
		this.availableVehicles = availableVehicles;
	}
	public AvailableVehicleDTO() {
		super();
	}
	@Override
	public String toString() {
		return "AvailableVehicleDTO [c=" + c + ", distance=" + distance + ", sourceloc=" + sourceloc
				+ ", destinationloc=" + destinationloc + ", penaltyamount=" + penaltyamount + ", availableVehicles="
				+ availableVehicles + "]";
	}
	
}
