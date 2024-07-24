package com.example.datn.restcontroller;

import com.example.datn.dto.KichCoDTO;
import com.example.datn.dto.MauSacDTO;
import com.example.datn.service.KichCoService;
import com.example.datn.service.MauSacService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kichco")
@RequiredArgsConstructor
public class KichCoResController {
    private final KichCoService kichCoService;

    @GetMapping("/getAll")
    public List<KichCoDTO> getAllMauSac() {
        return kichCoService.getAllKichCo();
    }

}
