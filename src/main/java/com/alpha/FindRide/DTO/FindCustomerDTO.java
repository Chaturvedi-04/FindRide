package com.alpha.FindRide.DTO;

public class FindCustomerDTO {
	
	private long mobileno;

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}


	public FindCustomerDTO(long mobileno) {
		super();
		this.mobileno = mobileno;
	}

	
	public FindCustomerDTO() {
		super();
	}

	@Override
	public String toString() {
		return "FindDriverDTO [mobileno=" + mobileno + "]";
	}
}
