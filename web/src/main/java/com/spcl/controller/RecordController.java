package com.spcl.controller;

import com.spcl.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/7.
 */
@Controller
public class RecordController {

    @RequestMapping( "/record/create" )
    public String create(@RequestParam String name) {
//		RestTemplate restTemplate = new RestTemplate();
        Map data = new HashMap<>();
        data.put("name",name);
        Record result = this.restTemplate.postForObject("http://DBService/record", null, Record.class,data);
//		Record result = restTemplate.getForObject("http://localhost:9501/dbhi", Record.class);
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());

        return "Create DBService  : "+result.getId()+" "+result.getName();
    }

    @RequestMapping(value = "/record/get",method = RequestMethod.GET)
    public String get(@RequestParam int id) {
//		RestTemplate restTemplate = new RestTemplate();

        Record result = this.restTemplate.getForObject("http://DBService/record/" + id, Record.class);
//		Record result = restTemplate.getForObject("http://localhost:9501/dbhi", Record.class);
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());

        return "Get DBService  : "+result.getId()+" "+result.getName();
    }

    @LoadBalanced
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RestTemplate restTemplate;
}
