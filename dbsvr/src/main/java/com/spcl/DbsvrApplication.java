package com.spcl;

import com.spcl.entity.Record;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//@EnableEurekaClient
@SpringBootApplication
@RestController
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.spcl")
@MapperScan("com.spcl.dao")
public class DbsvrApplication {

	public static void main(String[] args) {
		System.out.print("\nspring-boot.run.profiles : "+System.getProperty("spring-boot.run.profiles")+"\n");
		System.out.flush();
		SpringApplication.run(DbsvrApplication.class, args);
	}
//	@Value("${server.port}")
//	String port;
//	@RequestMapping("/hi")
//	public String home(@RequestParam String name) {
//		return "hi "+name+",i am from port:" +port;
//	}

	@Autowired
	private DiscoveryClient discoveryClient;

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(
			@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	AtomicInteger rid = new AtomicInteger();

	@RequestMapping("/dbhi")
	public Record home() {
		return new Record(rid.incrementAndGet(),"time:["+new Date()+"]"+System.getProperty("spring-boot.run.profiles")+" "+System.getProperty("spring.profiles.active"));
	}
//	@RequestMapping("/hi")
//	public String home() {
//		return "hi , no paramters,I AM DBSVR";
//	}
}