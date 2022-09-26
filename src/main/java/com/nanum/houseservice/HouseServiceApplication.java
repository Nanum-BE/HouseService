package com.nanum.houseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.nanum")
@EnableDiscoveryClient
@EnableJpaAuditing
public class HouseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseServiceApplication.class, args);
	}

}
