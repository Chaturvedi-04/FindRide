package com.alpha.FindRide.DTO;

import java.util.List;

public class BookingHistoryDTO {
	
	private List<RidedetailDTO> history;
	private double totalamount;
	public List<RidedetailDTO> getHistory() {
		return history;
	}
	public void setHistory(List<RidedetailDTO> history) {
		this.history = history;
	}
	public double getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}
	public BookingHistoryDTO(List<RidedetailDTO> history, double totalamount) {
		super();
		this.history = history;
		this.totalamount = totalamount;
	}
	public BookingHistoryDTO() {
		super();
	}
	@Override
	public String toString() {
		return "BookingHistoryDTO [history=" + history + ", totalamount=" + totalamount + "]";
	}
}
