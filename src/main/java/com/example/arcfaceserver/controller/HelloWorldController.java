package com.example.arcfaceserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @RequestMapping("hello")
    String hello() {
        return "Hello ,I am DragonForest!";
    }
}
