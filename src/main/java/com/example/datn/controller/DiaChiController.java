package com.example.datn.controller;

import com.example.datn.Repository.DiaChiRepository;
import com.example.datn.entity.DiaChiEntity;
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
public class DiaChiController {
    @Autowired
    DiaChiRepository diaChiRepository;

    @GetMapping("/diachi/getAll")
    public String getAllDiaChi(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DiaChiEntity> diaChiList = diaChiRepository.findAll(pageable);
        model.addAttribute("diaChiList", diaChiList);
        model.addAttribute("diaChi", new DiaChiEntity());
        return "admin/adminWeb/DiaChi";
    }

    @PostMapping("/diachi/add")
    public String addDiaChi(@Valid @ModelAttribute("diachi") DiaChiEntity diachi, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<DiaChiEntity> diaChiList = diaChiRepository.findAll();
            model.addAttribute("diaChiList", diaChiList);
            return "admin/adminWeb/DiaChi";
        }
        try {
            diaChiRepository.save(diachi);
            System.out.println(diachi);
            return "redirect:/diachi/getAll";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //
    @PostMapping("/diachi/delete/{id}")
    public String deleteChatLieu(@PathVariable("id") UUID id, Model model) {
        try {
            //Kiểm tra xem nó có tồn tại không
            if (!diaChiRepository.existsById(id)) {
                // Nếu không tồn tại
                model.addAttribute("errorMessage", "Không tìm thấy Chất liệu này");
                List<DiaChiEntity> diaChiList = diaChiRepository.findAll();
                model.addAttribute("diaChiList", diaChiList);
                return "admin/adminWeb/DiaChi";
            }
            //Xóa
            diaChiRepository.deleteById(id);
            // Sau khi xóa thành công quay lại trang getAll
            return "redirect:/diachi/getAll";
        } catch (Exception e) {
            // Trả ra thông báo nếu có ngoại lệ
            model.addAttribute("errorMessage", "Đã xảy ra lỗi vui lòng thử lại");
            List<DiaChiEntity> diaChiList = diaChiRepository.findAll();
            model.addAttribute("diaChiList", diaChiList);
            return "admin/adminWeb/DiaChi";
        }
    }

    @GetMapping("/diachi/detail/{id}")
    public String getDiaChiDetail(@PathVariable("id") UUID id, Model model) {
        Optional<DiaChiEntity> diaChiOptional = diaChiRepository.findById(id);
        if (diaChiOptional.isPresent()) {
            DiaChiEntity diaChi = diaChiOptional.get();
            model.addAttribute("diaChi", diaChi);
            return "admin/adminWeb/DiaChiDetail";
        } else {
            model.addAttribute("errorMessage", "Địa chỉ không tồn tại.");
            return "admin/adminWeb/DiaChi";
        }
    }

    @PostMapping("/diachi/update/{id}")
    public String updateChatLieu(@PathVariable("id") UUID id, @ModelAttribute("chatLieu") DiaChiEntity updateDiaChi, Model model) {
        try {
            // Kiểm tra xem chất liệu có tồn tại không
            if (!diaChiRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Chất liệu không tồn tại.");
                List<DiaChiEntity> diaChiList = diaChiRepository.findAll();
                model.addAttribute("diaChiList", diaChiList);
                return "admin/adminWeb/DiaChi";
            }

            // Lấy thông tin chất liệu cần cập nhật từ cơ sở dữ liệu
            DiaChiEntity existingdiaChi = diaChiRepository.findById(id).orElse(null);
            if (existingdiaChi == null) {
                return "redirect:/diachi/getAll";
            }


            existingdiaChi.setDiaChi(updateDiaChi.getDiaChi());
            existingdiaChi.setTinh(updateDiaChi.getTinh());
            existingdiaChi.setUpdateDate(updateDiaChi.getUpdateDate());
            existingdiaChi.setTrangThai(updateDiaChi.getTrangThai());
            existingdiaChi.setHuyen(updateDiaChi.getHuyen());
            diaChiRepository.save(existingdiaChi);
            System.out.println(existingdiaChi);
            return "redirect:/diachi/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật chất liệu.");
            return "redirect:/diachi/detail";
        }
    }

}

