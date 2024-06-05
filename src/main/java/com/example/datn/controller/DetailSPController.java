package com.example.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetailSPController {
    @GetMapping("/DetailSP")
    public String home(){
        return "web/DetailSP.html";
    }
}
