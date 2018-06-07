package com.hmb.regcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(RegCenterApplication.class, args);
	}
}
