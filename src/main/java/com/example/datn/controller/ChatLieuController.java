package com.example.datn.controller;

import com.example.datn.Repository.ChatLieuRepository;
import com.example.datn.entity.ChatLieuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ChatLieuController {

    @Autowired
    ChatLieuRepository chatLieuRepository;

    @GetMapping("/chatlieu/getAll")
    public String getAllChatLieu(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        if (page < 0) {
            page = 0; // Đảm bảo chỉ số trang không nhỏ hơn 0
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatLieuEntity> chatLieuPage = chatLieuRepository.findAll(pageable);
        model.addAttribute("chatLieuPage", chatLieuPage);
        model.addAttribute("chatLieu", new ChatLieuEntity());
        return "admin/adminWeb/ChatLieu";
    }

    @PostMapping("/chatlieu/add")
    public String addChatLieu(@Valid @ModelAttribute("chatLieu") ChatLieuEntity chatLieu, BindingResult result, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ChatLieuEntity> chatLieuPage = chatLieuRepository.findAll(pageable);
            model.addAttribute("chatLieuPage", chatLieuPage);
            return "admin/adminWeb/ChatLieu";
        }
        try {
            chatLieuRepository.save(chatLieu);
            return "redirect:/chatlieu/getAll";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/chatlieu/delete/{id}")
    public String deleteChatLieu(@PathVariable("id") UUID id, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        try {
            if (!chatLieuRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Không tìm thấy Chất liệu này");
                Pageable pageable = PageRequest.of(page, size);
                Page<ChatLieuEntity> chatLieuPage = chatLieuRepository.findAll(pageable);
                model.addAttribute("chatLieuPage", chatLieuPage);
                return "admin/adminWeb/ChatLieu";
            }
            chatLieuRepository.deleteById(id);
            return "redirect:/chatlieu/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi vui lòng thử lại");
            Pageable pageable = PageRequest.of(page, size);
            Page<ChatLieuEntity> chatLieuPage = chatLieuRepository.findAll(pageable);
            model.addAttribute("chatLieuPage", chatLieuPage);
            return "admin/adminWeb/ChatLieu";
        }
    }

    @GetMapping("/chatlieu/detail/{id}")
    public String getChatLieuDetail(@PathVariable("id") UUID id, Model model) {
        Optional<ChatLieuEntity> chatLieuOptional = chatLieuRepository.findById(id);
        if (chatLieuOptional.isPresent()) {
            ChatLieuEntity chatLieu = chatLieuOptional.get();
            model.addAttribute("chatLieu", chatLieu);
            return "admin/adminWeb/ChatLieuDetail";
        } else {
            model.addAttribute("errorMessage", "Material not found.");
            return "admin/adminWeb/ChatLieu";
        }
    }

    @PostMapping("/chatlieu/update/{id}")
    public String updateChatLieu(@PathVariable("id") UUID id, @ModelAttribute("chatLieu") ChatLieuEntity updatedChatLieu, Model model) {
        try {
            if (!chatLieuRepository.existsById(id)) {
                model.addAttribute("errorMessage", "Chất liệu không tồn tại.");
                Pageable pageable = PageRequest.of(0, 5);
                Page<ChatLieuEntity> chatLieuPage = chatLieuRepository.findAll(pageable);
                model.addAttribute("chatLieuPage", chatLieuPage);
                return "admin/adminWeb/ChatLieuDetail";
            }
            ChatLieuEntity existingChatLieu = chatLieuRepository.findById(id).orElse(null);
            if (existingChatLieu == null) {
                return "redirect:/chatlieu/getAll";
            }
            existingChatLieu.setTen(updatedChatLieu.getTen());
            existingChatLieu.setUpdateDate(updatedChatLieu.getUpdateDate());
            existingChatLieu.setTrangThai(updatedChatLieu.getTrangThai());
            chatLieuRepository.save(existingChatLieu);
            return "redirect:/chatlieu/getAll";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật chất liệu.");
            return "redirect:/chatlieu/detail";
        }
    }
}
