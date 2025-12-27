import { apiPost, apiPut, apiGet } from "./api.js";
import { $, toast, showJSON } from "./ui.js";

window.registerDriver = async function(){
    const res = await apiPost("/driver/saveDriver",{
        driverName: d_name.value,
        age: d_age.value,
        mobileno: d_mobile.value,
        gender: d_gender.value,
        mailid: d_email.value,
        password: d_pass.value,
        upiid: d_upi.value,

        vehicleName: v_name.value,
        vehicleNo: v_no.value,
        vehicleType: v_type.value,
        model: v_model.value,
        vehicleCapacity: v_capacity.value,

        latitude: v_lat.value,
        longitude: v_lon.value,
        pricePerKM: v_price.value,
        averageSpeed: v_speed.value
    });
    toast(res.message);
};

window.setAvailable = async function(status){
    await apiPut(`/driver/auth/updateStatus?mobileno=${d_mobile.value}&status=${status}`);
    toast("Status Updated");
};

window.activeD = async function(){
    const res = await apiGet(`/driver/auth/seeActiveBooking?mobileno=${d_mobile.value}`);
    showJSON("driverActive",res.data);
};

window.startRide = async function(){
    await apiPost(`/driver/auth/startride?otp=${otp_start.value}&bookingid=${booking_id.value}`);
    toast("OTP Verified Ride Started");
};

window.payCash = async function(){
    await apiPost(`/driver/auth/completeride/payByCash?bookingid=${booking_id.value}&paytype=CASH&otp=${otp_pay.value}`);
};

window.payUpi = async function(){
    const res = await apiPost(`/driver/auth/completeride/payByUpi?bookingid=${booking_id.value}&paytype=UPI`);
    showJSON("qrArea",res);
};

window.confirmPayment = async function(){
    await apiPost(`/driver/auth/ridecompleted/paymentconfirmed?bookingid=${booking_id.value}&paytype=CASH&otp=${otp_pay.value}`);
};

window.historyD = async function(){
    const res = await apiGet(`/driver/auth/seeBookingHistory?mobileno=${d_mobile.value}`);
    showJSON("driverHistory",res.data);
};
