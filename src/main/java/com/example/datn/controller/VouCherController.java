package com.example.datn.controller;

import com.example.datn.Repository.VouCherRepository;
import com.example.datn.entity.VouCherEntity;
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
public class VouCherController {
    @Autowired
    VouCherRepository vouCherRepository;

    @GetMapping("/voucher/getAll")
    public String getAllSanPham(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VouCherEntity> listVoucher = vouCherRepository.findAll(pageable);
        model.addAttribute("listVoucher", listVoucher);
        model.addAttribute("vouCher", new VouCherEntity());
        return "admin/adminWeb/voucher";
    }

    @PostMapping("/voucher/add")
    public String addDiaChi(@Valid @ModelAttribute("vouCher") VouCherEntity vouCher, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<VouCherEntity> listVoucher = vouCherRepository.findAll();
            model.addAttribute("listVoucher", listVoucher);
            return "admin/adminWeb/VouCher";
        }
        try {
            vouCherRepository.save(vouCher);
            System.out.println(vouCher);
            return "redirect:/voucher/getAll";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    //
    @PostMapping("/voucher/delete/{id}")
    public String deleteVouCher(@PathVariable("id") UUID id, Model model) {
        try {
            //Kiểm tra xem nó có tồn tại không
            if (!vouCherRepository.existsById(id)) {
                // Nếu không tồn tại
                model.addAttribute("errorMessage", "Không tìm thấy Chất liệu này");
                List<VouCherEntity> listVoucher = vouCherRepository.findAll();
                model.addAttribute("listVoucher", listVoucher);
                return "admin/adminWeb/VouCher";
            }
            //Xóa
            vouCherRepository.deleteById(id);
            // Sau khi xóa thành công quay lại trang getAll
            return "redirect:/voucher/getAll";
        } catch (Exception e) {
            // Trả ra thông báo nếu có ngoại lệ
            model.addAttribute("errorMessage", "Đã xảy ra lỗi vui lòng thử lại");
            List<VouCherEntity> listVoucher = vouCherRepository.findAll();
            model.addAttribute("listVoucher", listVoucher);
            return "admin/adminWeb/VouCher";
        }
    }
    @GetMapping("/voucher/detail/{id}")
    public String getDiaChiDetail(@PathVariable("id") UUID id, Model model) {
        Optional<VouCherEntity> vouCherOptional = vouCherRepository.findById(id);
        if (vouCherOptional.isPresent()) {
            VouCherEntity vouCher = vouCherOptional.get();
            model.addAttribute("vouCher", vouCher);
            return "admin/adminWeb/VouCherDetail";
        } else {
            model.addAttribute("errorMessage", "Địa chỉ không tồn tại.");
            return "admin/adminWeb/VouCher";
        }
    }

    @PostMapping("/voucher/update/{id}")
    public String updateChatLieu(@PathVariable("id") UUID id, @ModelAttribute("chatLieu") VouCherEntity updateVouCher, Model model) {
        try {
            // Kiểm tra xem chất liệu có tồn tại không
            if (!vouCherRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Chất liệu không tồn tại.");
                List<VouCherEntity> listVoucher = vouCherRepository.findAll();
                model.addAttribute("listVoucher", listVoucher);
                return "admin/adminWeb/VouCher";
            }

            // Lấy thông tin chất liệu cần cập nhật từ cơ sở dữ liệu
            VouCherEntity existingVouCher = vouCherRepository.findById(id).orElse(null);
            if (existingVouCher == null) {
                return "redirect:/voucher/getAll";
            }

            // Cập nhật thông tin chất liệu
            existingVouCher.setTen(updateVouCher.getTen());
            existingVouCher.setPhanTramGiam(updateVouCher.getPhanTramGiam());
            existingVouCher.setNgayBatDau(updateVouCher.getNgayBatDau());
            existingVouCher.setNgayKetThuc(updateVouCher.getNgayKetThuc());
            existingVouCher.setTrangThai(updateVouCher.getTrangThai());
            existingVouCher.setUpdateDate(updateVouCher.getUpdateDate());

            vouCherRepository.save(existingVouCher);
            return "redirect:/voucher/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật chất liệu.");
            return "redirect:/voucher/detail";
        }
    }
}
