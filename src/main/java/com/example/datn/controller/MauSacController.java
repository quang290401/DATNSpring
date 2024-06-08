package com.example.datn.controller;
import com.example.datn.Repository.MauSacRepository;
import com.example.datn.entity.MauSacEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MauSacController {
    @Autowired
    MauSacRepository mauSacRepository;

    @GetMapping("/mausac/getAll")
    public String getAllMauSac(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MauSacEntity> listMauSac = mauSacRepository.findAll(pageable);
        model.addAttribute("listMauSac", listMauSac);
        model.addAttribute("mauSac", new MauSacEntity());
        return "admin/adminWeb/MauSac";
    }
    @PostMapping("/mausac/add")
    public String addMauSac(@Valid @ModelAttribute("kichCo") MauSacEntity mauSac, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<MauSacEntity> listMauSac = mauSacRepository.findAll();
            model.addAttribute("listMauSac", listMauSac);
            return "admin/adminWeb/MauSac";
        }
//        chatLieu.setId(UUID.randomUUID());
        try {
            mauSacRepository.save(mauSac);
            return "redirect:/mausac/getAll";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/mausac/delete/{id}")
    public String deleteMauSac(@PathVariable("id") UUID id, Model model) {
        try {
            //Kiểm tra xem nó có tồn tại không
            if (!mauSacRepository.existsById(id)) {
                // Nếu không tồn tại
                model.addAttribute("errorMessage", "Không tìm thấy Chất liệu này");
                List<MauSacEntity> listMauSac = mauSacRepository.findAll();
                model.addAttribute("listMauSac", listMauSac);
                return "admin/adminWeb/MauSac";
            }
            //Xóa
            mauSacRepository.deleteById(id);
            // Sau khi xóa thành công quay lại trang getAll
            return "redirect:/mausac/getAll";
        } catch (Exception e) {
            // Trả ra thông báo nếu có ngoại lệ
            model.addAttribute("errorMessage", "Đã xảy ra lỗi vui lòng thử lại");
            List<MauSacEntity> listMauSac = mauSacRepository.findAll();
            model.addAttribute("listMauSac", listMauSac);
            return "admin/adminWeb/MauSac";
        }
    }
    @GetMapping("/mausac/detail/{id}")
    public String getMauSacDetail(@PathVariable("id") UUID id, Model model) {
        Optional<MauSacEntity> mauSacOptional = mauSacRepository.findById(id);
        if (mauSacOptional.isPresent()) {
            MauSacEntity mauSac = mauSacOptional.get();
            model.addAttribute("mauSac", mauSac);
            return "admin/adminWeb/MauSacDetail"; //
        } else {
            model.addAttribute("errorMessage", "Material not found.");
            return "admin/adminWeb/MauSac";// Hoặc trả về trang danh sách nếu không tìm thấy đối tượng
        }
    }
    @PostMapping("/mausac/update/{id}")
    public String updateKichCo(@PathVariable("id") UUID id, @ModelAttribute("chatLieu") MauSacEntity updateMauSac, Model model) {
        try {
            // Kiểm tra xem chất liệu có tồn tại không
            if (!mauSacRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Chất liệu không tồn tại.");
                List<MauSacEntity> listMauSac = mauSacRepository.findAll();
                model.addAttribute("listMauSac", listMauSac);
                return "admin/adminWeb/MauSacDetail";
            }

            // Lấy thông tin chất liệu cần cập nhật từ cơ sở dữ liệu
            MauSacEntity existingMauSac = mauSacRepository.findById(id).orElse(null);
            if (existingMauSac == null) {
                return "redirect:/mausac/getAll";
            }

            // Cập nhật thông tin chất liệu
            existingMauSac.setTen(updateMauSac.getTen());
            existingMauSac.setUpdateDate(updateMauSac.getUpdateDate());
            existingMauSac.setTrangThai(updateMauSac.getTrangThai());
            mauSacRepository.save(existingMauSac);
            return "redirect:/mausac/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật chất liệu.");
            return "redirect:/mausac/detail";
        }
    }
}
