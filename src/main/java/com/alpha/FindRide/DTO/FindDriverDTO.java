package com.alpha.FindRide.DTO;

public class FindDriverDTO {
	
	private long mobileno;

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public FindDriverDTO(long mobileno) {
		super();
		this.mobileno = mobileno;
	}

	public FindDriverDTO() {
		super();
	}

	@Override
	public String toString() {
		return "FindDriverDTO [mobileno=" + mobileno + "]";
	}
}
