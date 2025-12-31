package com.alpha.FindRide.DTO;

public class LoginRequestDTO {
	private long mobileno;
    private String password;

    public long getMobileno() {
        return mobileno;
    }

    public void setMobileno(long mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}