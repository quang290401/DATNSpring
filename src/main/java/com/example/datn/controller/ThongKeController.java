package com.example.datn.controller;

import com.example.datn.Repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/thongke")
public class ThongKeController {
    @Autowired
    private HoaDonRepository hoaDonRepository;

//    @GetMapping("/theothang")
//    public List<Object[]> thongKeTheoThang() {
//        return hoaDonRepository.thongKeTheoThang();
//    }
//    @GetMapping("/sanphambanchay")
//    public List<Object[]> sanPhamBanChay() {
//        return hoaDonRepository.sanPhamBanChay();
//    }
}
