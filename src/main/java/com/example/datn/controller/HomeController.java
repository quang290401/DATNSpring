package com.example.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/trang-chu")
    public String home(){
        return "web/web.html";
    }
}
