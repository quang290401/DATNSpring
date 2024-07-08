package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
//import com.example.datn.Repository.GioHangChiTietRepository;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BanHangOff {

    @Autowired
    private ChiTietSPRepository chiTietSPRepository;

//    @Autowired
//    private GioHangChiTietRepository gioHangChiTietRepository;

    @GetMapping("/BanHangOff")
    public String DSSanPham(Model model){
        List<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.findAll();
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
        return "/admin/adminWeb/BanHangOff";
    }

    @GetMapping("/BanHangOff/page")
    public String PhanTrang(@RequestParam(value = "so", defaultValue = "0", required = false) Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 6);
        Page<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.pageAll(pageable);
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
        return "/admin/adminWeb/BanHangOff";
    }

    @GetMapping("/BanHangOff/Search")
    public String Search(@RequestParam("ten") String ten, Model model){
        List<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.getData(ten);
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
        return "/admin/adminWeb/BanHangOff";
    }

    @GetMapping("/BanHangOff/Search/page")
    public String SearchPage(@RequestParam(value = "so", defaultValue = "0", required = false) Optional<Integer> page, @RequestParam("ten") String ten, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 6);
        Page<SanPhamChiTietEntity> sanPhamChiTiet = chiTietSPRepository.pageSearch(pageable, ten);
        model.addAttribute("sanPhamChiTiet", sanPhamChiTiet);
        model.addAttribute("SanPham", new SanPhamChiTietEntity());
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
//    @PostMapping("/GioHang/add")
//    public String addGioHang(@Valid @ModelAttribute ("GioHang")GioHangChiTietEntity gioHangChiTietEntity, BindingResult result, Model model){
//        if (result.hasErrors()) {
//            List<GioHangChiTietEntity> gioHangChiTietEntityList = gioHangChiTietRepository.findAll();
//            model.addAttribute("gioHangChiTietEntityList", gioHangChiTietEntityList);
//            return "/admin/adminWeb/BanHangOff";
//        }
//        try {
//            gioHangChiTietRepository.save(gioHangChiTietEntity);
//            return "redirect:/admin/adminWeb/BanHangOff";
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
