package com.spcl.web;

import com.spcl.entity.Record;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/6/7.
 * 这里使用@Controller而不用@RESTController是因为这里返回一个页面而不是一个值，
 * 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面，
 * 配置的视图解析器InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容。
 */
@Controller
public class ThymeleafTest
{
    AtomicInteger idGen = new AtomicInteger();
    @RequestMapping("/")
    public String index(Model model){
        List<Record> recordList = new Vector<>();
        for(int i=0;i<3;i++)
            recordList.add( new Record(idGen.incrementAndGet(),new Date().toString()));
        model.addAttribute("record",new Record(idGen.incrementAndGet(),new Date().toString()));
        model.addAttribute("recordList",recordList);
        return "index";
    }
}
