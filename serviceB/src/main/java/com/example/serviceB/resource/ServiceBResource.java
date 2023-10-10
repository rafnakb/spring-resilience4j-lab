package com.example.serviceB.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-b")
public class ServiceBResource {

    @GetMapping()
    public String serviceB(){
        return "Welcome to service B";
    }

}
