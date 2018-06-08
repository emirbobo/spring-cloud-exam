package com.spcl.controller;

import com.spcl.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by Administrator on 2018/6/7.
 */
@Controller
public class RecordController {

    @RequestMapping( "/record/create" )
    public String create(@RequestParam String name, Model model) {
//		RestTemplate restTemplate = new RestTemplate();
//        Map data = new HashMap<>();
//        data.put("name",name);
//        Record result = this.restTemplate.postForObject("http://DBService/record", null, Record.class,data);
        Record record = new Record();
        record.setName(name);
//        Record result = this.restTemplate.postForObject("http://DBService/record/crt", record, Record.class,data);
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("name", name);
        Record result = this.restTemplate.postForObject("http://DBService/record", requestEntity, Record.class);
//        HttpEntity<Foo> request = new HttpEntity<>(new Foo("bar"));
//        Record result = this.restTemplate.postForObject("http://DBService/record/crt", data, Record.class);
//		Record result = restTemplate.getForObject("http://localhost:9501/dbhi", Record.class);
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());

        model.addAttribute("record",result);
        return "record";
//        return "Create DBService  : "+result.getId()+" "+result.getName();
    }

    @RequestMapping(value = "/record/get",method = RequestMethod.GET)
    public String get(@RequestParam int id , Model model) {
//		RestTemplate restTemplate = new RestTemplate();
        Map vars = new HashMap<>();
        vars.put("id",id);
        Record result = this.restTemplate.getForObject("http://DBService/record/{id}", Record.class,vars);
//		Record result = restTemplate.getForObject("http://localhost:9501/dbhi", Record.class);
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());



        model.addAttribute("record",result);
        return "record";
//        return "Get DBService  : "+result.getId()+" "+result.getName();
    }



    @RequestMapping("/hi")
    public String home() {
//		RestTemplate restTemplate = new RestTemplate();
        Record result = this.restTemplate.getForObject("http://DBService/dbhi", Record.class);
//		Record result = restTemplate.getForObject("http://localhost:9501/dbhi", Record.class);
//		Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//		log.info(quote.toString());

        return "From DBService  : "+result.getName();
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
