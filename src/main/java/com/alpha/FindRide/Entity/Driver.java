package com.alpha.FindRide.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Driver {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long licenseNo;
	private String upiid;
	private String name;
	private String status = "Available";
	private int age;
	private long mobileno;
	private String gender;
	private String mailid;
	
	@OneToOne(optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private AppUser user;
	
	@OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
	private Vehicle vehicle;

	@OneToMany
	private List<Booking> bookingList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(long licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getUpiid() {
		return upiid;
	}

	public void setUpiid(String upiid) {
		this.upiid = upiid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMailid() {
		return mailid;
	}

	public void setMailid(String mailid) {
		this.mailid = mailid;
	}

	public AppUser getAppUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public List<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Booking> bookingList) {
		this.bookingList = bookingList;
	}

	public Driver(long licenseNo, String upiid, String name, String status, int age, long mobileno, String gender,
			String mailid, AppUser user, Vehicle vehicle, List<Booking> bookingList) {
		super();
		this.licenseNo = licenseNo;
		this.upiid = upiid;
		this.name = name;
		this.status = status;
		this.age = age;
		this.mobileno = mobileno;
		this.gender = gender;
		this.mailid = mailid;
		this.user = user;
		this.vehicle = vehicle;
		this.bookingList = bookingList;
	}

	public Driver() {
		super();
	}

	@Override
	public String toString() {
		return "Driver [id=" + id + ", licenseNo=" + licenseNo + ", upiid=" + upiid + ", name=" + name + ", status="
				+ status + ", age=" + age + ", mobileno=" + mobileno + ", gender=" + gender + ", mailid=" + mailid
				+ ", user=" + user + ", vehicle=" + vehicle + ", bookingList=" + bookingList + "]";
	}

}
