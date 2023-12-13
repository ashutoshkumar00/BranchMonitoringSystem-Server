package com.hdfc.AdminCredentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdminCrendentialsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminCrendentialsApplication.class, args);
	}

}
