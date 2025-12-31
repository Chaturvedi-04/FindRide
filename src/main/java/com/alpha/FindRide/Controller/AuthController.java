package com.alpha.FindRide.Controller;

import com.alpha.FindRide.DTO.LoginRequestDTO;
import com.alpha.FindRide.DTO.RegisterCustomerDTO;
import com.alpha.FindRide.DTO.RegisterDriverVehicleDTO;
import com.alpha.FindRide.ResponseStructure;
import com.alpha.FindRide.Service.CustomerService;

import com.alpha.FindRide.Service.DriverService;
import com.alpha.FindRide.Entity.AppUser;
import com.alpha.FindRide.Entity.Customer;
import com.alpha.FindRide.Entity.Driver;
import com.alpha.FindRide.Exceptions.AppUserNotFoundException;
import com.alpha.FindRide.Repository.AppUserRepo;
import com.alpha.FindRide.security.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Value("${locationiq.api.key}")
    private String apiKey;
	
    @Autowired
    private CustomerService cs;

    @Autowired
    private DriverService ds;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private AppUserRepo appuserrep;
    

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/test")
    public String test() {
        return "Security OK";
    }
	
    // ===============================
    // CUSTOMER REGISTRATION
    // ===============================
    @PostMapping("/register/customer")
    public ResponseEntity<ResponseStructure<Customer>> saveCustomer(@RequestBody RegisterCustomerDTO rdto) {

    	return cs.saveCustomer(rdto);
	}

    // ===============================
    // DRIVER REGISTRATION
    // ===============================
    @PostMapping("/register/driver")
    public ResponseEntity<ResponseStructure<Driver>> registerDriver(
            @RequestBody RegisterDriverVehicleDTO dto) {

        return  ds.saveDriver(dto);
    }

    // ===============================
    // LOGIN (JWT GENERATION)
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<String>> login(@RequestBody LoginRequestDTO dto) {

    	authenticationManager.authenticate(
    		    new UsernamePasswordAuthenticationToken(
    		        String.valueOf(dto.getMobileno()),
    		        dto.getPassword()
    		    )
    		);

        AppUser user = appuserrep.findByMobileno(dto.getMobileno()).orElseThrow(()-> new AppUserNotFoundException());

        String token = jwtUtils.generateToken(
                String.valueOf(user.getMobileno()),
                user.getRole()
        );

        ResponseStructure<String> rs = new ResponseStructure<>();
        rs.setStatuscode(HttpStatus.OK.value());
        rs.setMessage("Login successful");
        rs.setData("Bearer " + token);

        return ResponseEntity.ok(rs);
    }
}
