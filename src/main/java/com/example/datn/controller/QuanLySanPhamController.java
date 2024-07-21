package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class QuanLySanPhamController {

//    @Autowired
//    ChiTietSPRepository chiTietSPRepository;
//
//    @GetMapping("admin")
//    public String getAll(Model model){
//        List<SanPhamChiTietEntity> sanPhamList = chiTietSPRepository.findAll();
//        model.addAttribute("sanPhamList", sanPhamList);
//        model.addAttribute("sanPham", new SanPhamChiTietEntity());
//        return "admin/adminWeb/Admin";
//    }
}
