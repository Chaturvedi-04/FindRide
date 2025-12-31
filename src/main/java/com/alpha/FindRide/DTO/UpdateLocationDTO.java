package com.alpha.FindRide.DTO;

public class UpdateLocationDTO {
	
	private String latitude;
	private String longitude;
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public UpdateLocationDTO( String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public UpdateLocationDTO() {
		super();
	}
	@Override
	public String toString() {
		return "UpdateLocationDTO [ latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
