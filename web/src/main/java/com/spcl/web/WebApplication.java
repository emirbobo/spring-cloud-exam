package com.spcl.web;

import com.spcl.entity.Record;
import com.spcl.util.UtilSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.*;

import java.nio.file.Paths;

@RestController
@SpringBootApplication
@RibbonClient(name = "DBClient", configuration = DBConfiguration.class)
@ComponentScan(basePackages = "com.spcl")
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}


//	@RequestMapping("/hi")
//	public String home(@RequestParam String name) {
//		return "hi "+name+",i am out";
//	}

//	@Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.build();
//	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public AbstractConfigurableTemplateResolver templateResolver(){
		AbstractConfigurableTemplateResolver templateResolver = UtilSystem.getTemplateResolver();

		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		// SpringTemplateEngine automatically applies SpringStandardDialect and
		// enables Spring's own MessageSource message resolution mechanisms.
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		// Enabling the SpringEL compiler with Spring 4.2.4 or newer can
		// speed up execution in most scenarios, but might be incompatible
		// with specific cases when expressions in one template are reused
		// across different data types, so this flag is "false" by default
		// for safer backwards compatibility.
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}
}
