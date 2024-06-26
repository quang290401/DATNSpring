package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
import com.example.datn.entity.ChatLieuEntity;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BanHangOff {

    @Autowired
    private ChiTietSPRepository chiTietSPRepository;

    @GetMapping("/BanHangOff")
    public String DSSanPham(Model model){
        List<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.findAll();
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
//        model.addAttribute("helloworld", chiTietSPRepository.findAllActiveUsersNative());
        return "/admin/adminWeb/BanHangOff";
    }
    @GetMapping("/Detail/{id}")
    public String Detail(@PathVariable("id") UUID id,Model model){
        Optional<SanPhamChiTietEntity> SPCTOptinal = chiTietSPRepository.findById(id);
        if (SPCTOptinal.isPresent()) {
            SanPhamChiTietEntity ctspOp = SPCTOptinal.get();
            model.addAttribute("sanpham", ctspOp);
            return "/admin/adminWeb/BanHangOffDetail"; //
        } else {
            model.addAttribute("errorMessage", "Material not found.");
            return "/admin/adminWeb/BanHangOff";// Hoặc trả về trang danh sách nếu không tìm thấy đối tượng
        }

    }

}
