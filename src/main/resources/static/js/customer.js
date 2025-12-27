import { apiPost, apiGet } from "./api.js";
import { $, toast, showJSON } from "./ui.js";

window.registerCustomer = async function(){
    const res = await apiPost("/customer/saveCustomer",{
        name: c_name.value,
        age: c_age.value,
        gender: c_gender.value,
        password: c_pass.value,
        mobileno: c_mobile.value,
        emailid: c_email.value,
        latitude: c_lat.value,
        longitude: c_lon.value
    });
    toast(res.message);
};

window.getVehicles = async function(){
    const res = await apiGet(`/customer/auth/seeallAvailableVehicles?mobileno=${c_mobile.value}&destinationCity=${dest_city.value}`);
    renderVehicles(res.data.availableVehicles);
};

function renderVehicles(list){
    vehicleList.innerHTML = "";
    list.forEach(v=>{
        let b = document.createElement("button");
        b.className="success";
        b.innerText=`Book ${v.v.name} ₹${v.fare}`;
        b.onclick=()=>book(v.v.id,v.fare,v.estimatedtime);
        vehicleList.appendChild(b);
    });
}

window.book = async function(id,fare,eta){
    const res = await apiPost(`/bookVehicle?mobileno=${c_mobile.value}`,{
        vehicleid:id,
        sourceLoc:"AUTO",
        destinationLoc: dest_city.value,
        fare: fare,
        distanceTravelled:1,
        estimatedTime:eta
    });
    toast("Ride Booked");
};

window.active = async function(){
    const res = await apiGet(`/customer/auth/seeActiveBooking?mobileno=${c_mobile.value}`);
    showJSON("activeBook",res.data);
};

window.historyC = async function(){
    const res = await apiGet(`/customer/auth/seeBookingHistory?mobileno=${c_mobile.value}`);
    showJSON("custHistory",res.data);
};
