package com.wenli.springbootdemo;

import com.wenli.springbootdemo.config.SystemConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan({"com.wenli.springbootdemo.dao","com.wenli.springbootdemo.common.task"})
public class SpringbootdemoApplication {

    // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjM1OTQ1MTYsInVzZXJuYW1lIjoiUm9zcyJ9.u44kvwKmHKtR6eFskBwHlM8O7kuDijNaIfB9drlj0jQ
    @Autowired
    private Environment environment;

    @Autowired
    private SystemConfig systemConfig;

/*    @Value(value = "${app.info}")
    String appInfo;*/

    public static void main(String[] args) {
        SpringApplication.run(SpringbootdemoApplication.class, args);
    }

    /*@RequestMapping(value = "/hello")
    public String hello(){
        return appInfo;
    }*/

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return systemConfig.toString();
    }





}
