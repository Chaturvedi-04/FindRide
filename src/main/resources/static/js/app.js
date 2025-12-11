// app.js — lightweight client to call the backend REST endpoints

const apiRoot = ''; // same origin; keep empty if static served by Spring boot

/* Utility to show JSON nicely */
function pretty(obj){
  try { return JSON.stringify(obj, null, 2); }
  catch(e){ return String(obj); }
}

/* ---- Customer: register ---- */
const registerCustomerForm = document.getElementById('registerCustomerForm');
if(registerCustomerForm){
  registerCustomerForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const fd = new FormData(registerCustomerForm);
    const body = Object.fromEntries(fd.entries());
    const resEl = document.getElementById('registerCustomerResult');

    try {
      const resp = await fetch(apiRoot + '/customer/saveCustomer', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
      });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+ err;
    }
  });
}

/* ---- Customer: find ----
   NOTE: backend mapping should be POST, see recommended backend change above.
*/
const findCustomerForm = document.getElementById('findCustomerForm');
if(findCustomerForm){
  findCustomerForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const mob = findCustomerForm.mobileno.value;
    const resEl = document.getElementById('findCustomerResult');

    try {
      const resp = await fetch(apiRoot + '/customer/findCustomer', {
        method: 'POST', // requires backend change from GET->POST
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({mobileno: Number(mob)})
      });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+ err;
    }
  });

  // delete
  const deleteBtn = document.getElementById('deleteCustomerBtn');
  if(deleteBtn){
    deleteBtn.addEventListener('click', async ()=>{
      const mob = document.getElementById('deleteMobileno').value;
      const resEl = document.getElementById('findCustomerResult');
      if(!mob){ resEl.textContent = 'Please enter mobile number to delete.'; return; }
      try {
        const resp = await fetch(apiRoot + `/customer/deleteCustomer?mobileno=${encodeURIComponent(mob)}`, {
          method: 'DELETE'
        });
        const data = await resp.json();
        resEl.textContent = pretty(data);
      } catch (err){
        resEl.textContent = 'Error: '+ err;
      }
    });
  }
}

/* ---- See Available Vehicles ---- */
const seeVehiclesForm = document.getElementById('seeVehiclesForm');
if(seeVehiclesForm){
  seeVehiclesForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const fd = new FormData(seeVehiclesForm);
    const mob = fd.get('mobileno');
    const dest = fd.get('destinationCity');
    const resEl = document.getElementById('seeVehiclesResult');

    try {
      // endpoint expects POST with @RequestParam mobileno & destinationCity
      const url = apiRoot + `/customer/seeallAvailableVehicles?mobileno=${encodeURIComponent(mob)}&destinationCity=${encodeURIComponent(dest)}`;
      const resp = await fetch(url, { method:'POST' });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+err;
    }
  });
}

/* ---- Driver flows ---- */
const registerDriverForm = document.getElementById('registerDriverForm');
if(registerDriverForm){
  registerDriverForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const fd = new FormData(registerDriverForm);
    const body = Object.fromEntries(fd.entries());
    const resEl = document.getElementById('registerDriverResult');

    try {
      const resp = await fetch(apiRoot + '/saveDriver', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
      });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+err;
    }
  });
}

const findDriverForm = document.getElementById('findDriverForm');
if(findDriverForm){
  findDriverForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const mob = findDriverForm.mobileno.value;
    const resEl = document.getElementById('driverOpsResult');

    try {
      const resp = await fetch(apiRoot + '/findDriver', {
        method: 'POST', // recommended backend change from GET->POST
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({mobileno: Number(mob)})
      });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+err;
    }
  });
}

/* update driver location */
const updateLocationForm = document.getElementById('updateLocationForm');
if(updateLocationForm){
  updateLocationForm.addEventListener('submit', async (e)=>{
    e.preventDefault();
    const fd = new FormData(updateLocationForm);
    const body = Object.fromEntries(fd.entries());
    const resEl = document.getElementById('driverOpsResult');

    try {
      const resp = await fetch(apiRoot + '/updateLocation', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
      });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+err;
    }
  });
}

/* delete driver */
const deleteDriverBtn = document.getElementById('deleteDriverBtn');
if(deleteDriverBtn){
  deleteDriverBtn.addEventListener('click', async ()=>{
    const mob = document.getElementById('deleteDriverMobileno').value;
    const resEl = document.getElementById('driverOpsResult');
    if(!mob){ resEl.textContent = 'Enter mobile number to delete'; return; }
    try {
      const resp = await fetch(`/deleteDriver?mobileno=${encodeURIComponent(mob)}`, { method: 'DELETE' });
      const data = await resp.json();
      resEl.textContent = pretty(data);
    } catch (err){
      resEl.textContent = 'Error: '+err;
    }
  });
}
