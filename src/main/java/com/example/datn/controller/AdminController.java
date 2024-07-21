package com.example.datn.controller;

import com.example.datn.Repository.*;
import com.example.datn.entity.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {

    @Autowired
    ChiTietSPRepository chiTietSPRepository;

    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    MauSacRepository mauSacRepository;

    @Autowired
    ChatLieuRepository chatLieuRepository;

    @Autowired
    DanhMucRepository danhMucRepository;

    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    NSXRepository nsxRepository;
    @GetMapping("admin")
    public String home(Model model){
        List<SanPhamChiTietEntity> sanPhamList = chiTietSPRepository.findAll();
        model.addAttribute("sanPhamList", sanPhamList);
        model.addAttribute("sanPham", new SanPhamChiTietEntity());
        List<SanPhamEntity> sanPhamEntities = sanPhamRepository.findAll();
        model.addAttribute("tenSanPham",sanPhamEntities);
        List<MauSacEntity> mauSacEntities = mauSacRepository.findAll();
        model.addAttribute("tenMauSac",mauSacEntities);
        List<ChatLieuEntity> chatLieuEntities = chatLieuRepository.findAll();
        model.addAttribute("tenChatLieu",chatLieuEntities);
        List<DanhMucEntity> danhMucEntities = danhMucRepository.findAll();
        model.addAttribute("tenDanhMuc",danhMucEntities);
        List<KichCoEntity> kichCoEntities = kichCoRepository.findAll();
        model.addAttribute("tenKichCo",kichCoEntities);
        List<NSXEntity> nsxEntities = nsxRepository.findAll();
        model.addAttribute("tenNsx",nsxEntities);

        return "admin/adminWeb/Admin";
    }
    @PostMapping("admin/SanPhamAdd")
    public String add(@Valid @ModelAttribute("sanPham") SanPhamChiTietEntity sanPhamChiTiet, BindingResult result, Model model){
        if (result.hasErrors()) {
            List<SanPhamChiTietEntity> sanPhamList = chiTietSPRepository.findAll();
            model.addAttribute("sanPhamList", sanPhamList);
            return "admin/adminWeb/Admin";
        }
        try {
            chiTietSPRepository.save(sanPhamChiTiet);
            return "redirect:/admin";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/admin/delete/{id}")
    public String delete(@PathVariable("id") UUID id, Model model) {
        try {
            //Kiểm tra xem nó có tồn tại không
            if (!chiTietSPRepository.existsById(id)) {
                // Nếu không tồn tại
                model.addAttribute("errorMessage", "Không tìm thấy Chất liệu này");
                List<SanPhamChiTietEntity> sanPhamList = chiTietSPRepository.findAll();
                model.addAttribute("sanPhamList", sanPhamList);
                return "admin/adminWeb/Admin";
            }
            //Xóa
            chiTietSPRepository.deleteById(id);
            // Sau khi xóa thành công quay lại trang getAll
            return "redirect:/admin";
        } catch (Exception e) {
            // Trả ra thông báo nếu có ngoại lệ
            model.addAttribute("errorMessage", "Đã xảy ra lỗi vui lòng thử lại");
            List<ChatLieuEntity> chatLieuList = chatLieuRepository.findAll();
            model.addAttribute("chatLieuList", chatLieuList);
            return "admin/adminWeb/ChatLieu";
        }
    }
    @GetMapping("/admin/detail/{id}")
    public String getDetail(@PathVariable("id") UUID id, Model model) {
        Optional<SanPhamChiTietEntity> chiTietEntityOptional = chiTietSPRepository.findById(id);
        if (chiTietEntityOptional.isPresent()) {
            SanPhamChiTietEntity chiTiet = chiTietEntityOptional.get();
            model.addAttribute("chiTiet", chiTiet);
            return "admin/adminWeb/admin"; //
        } else {
            model.addAttribute("errorMessage", "Material not found.");
            return "admin/adminWeb/admin";// Hoặc trả về trang danh sách nếu không tìm thấy đối tượng
        }
    }
}
