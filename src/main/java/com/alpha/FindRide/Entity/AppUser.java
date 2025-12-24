package com.alpha.FindRide.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long mobileno;
	private String password;
	private String role;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public AppUser(int id, long mobileno, String password, String role) {
		super();
		this.id = id;
		this.mobileno = mobileno;
		this.password = password;
		this.role = role;
	}
	public AppUser() {
		super();
	}
	@Override
	public String toString() {
		return "AppUser [id=" + id + ", mobileno=" + mobileno + ", password=" + password + ", role=" + role + "]";
	}
}
