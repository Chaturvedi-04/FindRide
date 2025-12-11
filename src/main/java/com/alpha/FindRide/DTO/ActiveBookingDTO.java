package com.alpha.FindRide.DTO;

import com.alpha.FindRide.Entity.Booking;

public class ActiveBookingDTO {
	
	private String custname;
	private long custmobno;
	private Booking booking;
	private String currentlocation;
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	public long getCustmobno() {
		return custmobno;
	}
	public void setCustmobno(long custmobno) {
		this.custmobno = custmobno;
	}
	public Booking getBooking() {
		return booking;
	}
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	public String getCurrentlocation() {
		return currentlocation;
	}
	public void setCurrentlocation(String currentlocation) {
		this.currentlocation = currentlocation;
	}
	public ActiveBookingDTO(String custname, long custmobno, Booking booking, String currentlocation) {
		super();
		this.custname = custname;
		this.custmobno = custmobno;
		this.booking = booking;
		this.currentlocation = currentlocation;
	}
	public ActiveBookingDTO() {
		super();
	}
	@Override
	public String toString() {
		return "ActiveBookingDTO [custname=" + custname + ", custmobno=" + custmobno + ", booking=" + booking
				+ ", currentlocation=" + currentlocation + "]";
	}
}
