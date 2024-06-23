package com.example.datn.controller;

import com.example.datn.dto.UserDTO;
import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.entity.MauSacEntity;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.Repository.SanPhamChiTietRepository;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.service.SanPhamChiTietService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class DetailSPController {
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public DetailSPController(SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    @GetMapping("/detailSP/{id}")
    public String home(Model model, HttpSession session){
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "web/DetailSP.html";
    }

    @PostMapping("/detailSP/delete/{id}")
    public String deleteSanPham(@PathVariable("id") UUID id, Model model) {
        try {
            //Kiểm tra xem nó có tồn tại không
            if (!sanPhamChiTietRepository.existsById(id)) {
                // Nếu không tồn tại
                model.addAttribute("errorMessage", "Không tìm thấy CSSP này");
                List<SanPhamChiTietEntity> sanPhamList = sanPhamChiTietRepository.findAll();
                model.addAttribute("sanPhamList", sanPhamList);
                return "admin/adminWeb/SanPhamDetail";
            }
            //Xóa
            sanPhamChiTietRepository.deleteById(id);
            // Sau khi xóa thành công quay lại trang getAll
            return "redirect:/detailSP/getAll";
        } catch (Exception e) {
            // Trả ra thông báo nếu có ngoại lệ
            model.addAttribute("errorMessage", "Đã xảy ra lỗi, vui lòng thử lại");
            List<SanPhamChiTietEntity> sanPhamList = sanPhamChiTietRepository.findAll();
            model.addAttribute("sanPhamList", sanPhamList);
            return "admin/adminWeb/SanPhamDetail";
        }
    }

    @GetMapping("/detailSP/detail/{id}")
    public String getSanPhamDetail(@PathVariable("id") UUID id, Model model) {
        Optional<SanPhamChiTietEntity> detailSPOptional = sanPhamChiTietRepository.findById(id);
        if (detailSPOptional.isPresent()) {
            SanPhamChiTietEntity sanPham = detailSPOptional.get();
            model.addAttribute("sanPham", sanPham);
            return "admin/adminWeb/SanPhamDetail"; //
        } else {
            model.addAttribute("errorMessage", "Không tìm thấy CTSP.");
            return "admin/adminWeb/SanPhamDetail";
        }
    }

    @PostMapping("/sanpham/add")
    public String addChatLieu(@Valid @ModelAttribute("sanPham") SanPhamChiTietEntity sanPham, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<SanPhamChiTietEntity> sanPhamList = sanPhamChiTietRepository.findAll();
            model.addAttribute("sanPhamList", sanPhamList);
            return "admin/adminWeb/SanPhamDetail";
        }
        try {
            sanPhamChiTietRepository.save(sanPham);
            return "redirect:/SanPhamDetail/getAll";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
