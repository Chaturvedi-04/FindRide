package com.alpha.FindRide.DTO;

public class UpdateLocationDTO {
	
	private long mobileno;
	private String latitude;
	private String longitude;
	public long getMobileno() {
		return mobileno;
	}
	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}
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
	public UpdateLocationDTO(long mobileno, String latitude, String longitude) {
		super();
		this.mobileno = mobileno;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public UpdateLocationDTO() {
		super();
	}
	@Override
	public String toString() {
		return "UpdateLocationDTO [mobileno=" + mobileno + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
}
