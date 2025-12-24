package com.alpha.FindRide.DTO;

public class RegisterCustomerDTO {
	
	private String name;
	private int age;
	private String password;
	private String gender;
	private long mobileno;
	private String emailid;
	private String latitude;
	private String longitude;
	
	public RegisterCustomerDTO() {
		super();
	}

	public RegisterCustomerDTO(String name, int age, String password, String gender, long mobileno, String emailid,
			String latitude, String longitude) {
		super();
		this.name = name;
		this.age = age;
		this.password = password;
		this.gender = gender;
		this.mobileno = mobileno;
		this.emailid = emailid;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
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

	@Override
	public String toString() {
		return "RegisterCustomerDTO [name=" + name + ", age=" + age + ", password=" + password + ", gender=" + gender
				+ ", mobileno=" + mobileno + ", emailid=" + emailid + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
}
