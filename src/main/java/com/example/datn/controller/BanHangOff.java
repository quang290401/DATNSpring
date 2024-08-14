package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
import com.example.datn.Repository.HoaDonRepository;
import com.example.datn.entity.ChatLieuEntity;
import com.example.datn.entity.HoaDonEntity;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping()
public class BanHangOff {

    @Autowired
    private ChiTietSPRepository chiTietSPRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("/BanHangOff")
    public String DSSanPham(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "6") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPhamChiTietEntity> sanPhamChiTietPage = chiTietSPRepository.findAll(pageable);

        model.addAttribute("sanPhamChiTiet", sanPhamChiTietPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sanPhamChiTietPage.getTotalPages());

        return "/admin/adminWeb/BanHangOff";
    }
    @GetMapping("/thongke")
    public String thongKe() {
        return "/admin/adminWeb/ThongKeTheoThang";
    }
    @GetMapping("/sanpham")
    public String sanPham() {
        return "/admin/adminWeb/SanPhamBanChay";
    }
}
