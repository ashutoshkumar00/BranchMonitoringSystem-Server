package com.hdfc.AdminCredentials.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("EMPLOYEE-SERVICE")
public interface BranchInterface {
	
	@GetMapping("/getbranchnamebyid/{bid}")
	public String getBranchNameById(@PathVariable Long bid,@RequestHeader String Authorization);

}
