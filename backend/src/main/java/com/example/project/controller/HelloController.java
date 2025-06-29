package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")  // Đảm bảo có mapping "/" đúng
    public String hello() {
        return "Xin chào từ Spring Boot!";
    }
}
