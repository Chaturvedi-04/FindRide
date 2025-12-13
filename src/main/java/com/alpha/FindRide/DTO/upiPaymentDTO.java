package com.alpha.FindRide.DTO;

import java.util.Arrays;

public class upiPaymentDTO {

	private double fare;
	private byte[] qr;
	public upiPaymentDTO(double fare, byte[] qr) {
		super();
		this.fare = fare;
		this.qr = qr;
	}
	public upiPaymentDTO() {
		super();
	}
	public double getFare() {
		return fare;
	}
	public void setFare(double fare) {
		this.fare = fare;
	}
	public byte[] getQr() {
		return qr;
	}
	public void setQr(byte[] qr) {
		this.qr = qr;
	}
	@Override
	public String toString() {
		return "upiPaymentDTO [fare=" + fare + ", qr=" + Arrays.toString(qr) + "]";
	}
	
}
