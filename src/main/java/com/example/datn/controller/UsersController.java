package com.example.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class UsersController {
    @GetMapping("/quanly-user")
    public String home(){
        return "admin/adminWeb/Users .html";
    }
}
