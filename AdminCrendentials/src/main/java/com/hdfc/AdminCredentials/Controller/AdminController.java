package com.hdfc.AdminCredentials.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.AdminCredentials.Exception.HandleBadRequest;
import com.hdfc.AdminCredentials.Model.Admin;
import com.hdfc.AdminCredentials.Service.AdminService;
import com.hdfc.AdminCredentials.Service.JWTService;
import com.hdfc.AdminCredentials.dto.AdminRequest;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:3000/")
public class AdminController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/welcome")
	public String home() {
		return "welcome to home controller";
	}
	
	@PostMapping("/addadmin")
	public Admin addAdmin(@RequestBody Admin admin) {
		return adminService.addAdmin(admin);
	}
	
	@GetMapping("/getalladmin")
	@PreAuthorize("hasAuthority('admin')")
	public List<Admin> getAllAdmin(){
		return adminService.getAllAdmin();
	}
	
	@DeleteMapping("/deleteadmin/{id}")
	@PreAuthorize("hasAuthority('admin')")
	public String deleteAdminById(@PathVariable Long id) {
		return adminService.deleteAdminById(id);
	}
	
	@PostMapping("/authenticate")
	public Map<String,String> authenticateAndGenerateJWTToken(@RequestBody AdminRequest auth) throws HandleBadRequest {
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getName(), auth.getPassword()));
		System.out.println(auth);
		if(authentication.isAuthenticated()) {
			return jwtService.generateJWTToken(auth.getName());
		}
		throw new HandleBadRequest("Invalid user request");
	}
	
	@GetMapping("/validatetoken")
	public Boolean validateToken(@RequestHeader String Authorization) {
		return true;
	}
	

}
