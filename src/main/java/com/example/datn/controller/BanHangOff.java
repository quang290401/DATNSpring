package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BanHangOff {

    @Autowired
    private ChiTietSPRepository chiTietSPRepository;

    @GetMapping("/BanHangOff")
    public String DSSanPham(Model model){
        List<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.findAll();
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
        return "/admin/adminWeb/BanHangOff";
    }
}
