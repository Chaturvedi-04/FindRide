package com.alpha.FindRide.DTO;

public class RegisterDriverVehicleDTO {

	private long licenseNo;
	private String upiid;
	private String driverName;
	private int age;
	private long mobileno;
	private String gender;
	private String mailid;
	private String password;
	
	private String vehicleName;
	private String vehicleNo;
	private String vehicleType;
	private String model;
	private int vehicleCapacity;
	private String latitude;
	private String longitude;
	private int pricePerKM;
	private double averageSpeed;
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
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVehicleName() {
		return vehicleName;
	}
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getVehicleCapacity() {
		return vehicleCapacity;
	}
	public void setVehicleCapacity(int vehicleCapacity) {
		this.vehicleCapacity = vehicleCapacity;
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
	public int getPricePerKM() {
		return pricePerKM;
	}
	public void setPricePerKM(int pricePerKM) {
		this.pricePerKM = pricePerKM;
	}
	public double getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public RegisterDriverVehicleDTO(long licenseNo, String upiid, String driverName, int age, long mobileno,
			String gender, String mailid, String password, String vehicleName, String vehicleNo, String vehicleType,
			String model, int vehicleCapacity, String latitude, String longitude, int pricePerKM, double averageSpeed) {
		super();
		this.licenseNo = licenseNo;
		this.upiid = upiid;
		this.driverName = driverName;
		this.age = age;
		this.mobileno = mobileno;
		this.gender = gender;
		this.mailid = mailid;
		this.password = password;
		this.vehicleName = vehicleName;
		this.vehicleNo = vehicleNo;
		this.vehicleType = vehicleType;
		this.model = model;
		this.vehicleCapacity = vehicleCapacity;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pricePerKM = pricePerKM;
		this.averageSpeed = averageSpeed;
	}
	public RegisterDriverVehicleDTO() {
		super();
	}
	@Override
	public String toString() {
		return "RegisterDriverVehicleDTO [licenseNo=" + licenseNo + ", upiid=" + upiid + ", driverName=" + driverName
				+ ", age=" + age + ", mobileno=" + mobileno + ", gender=" + gender + ", mailid=" + mailid
				+ ", password=" + password + ", vehicleName=" + vehicleName + ", vehicleNo=" + vehicleNo
				+ ", vehicleType=" + vehicleType + ", model=" + model + ", vehicleCapacity=" + vehicleCapacity
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", pricePerKM=" + pricePerKM
				+ ", averageSpeed=" + averageSpeed + "]";
	}	
}
