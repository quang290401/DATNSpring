package com.example.datn.restcontroller;

import com.example.datn.dto.DanhMucDTO;
import com.example.datn.dto.HinhAnhDTO;
import com.example.datn.service.DanhMucService;
import com.example.datn.service.HinhAnhService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hinhanh")
@RequiredArgsConstructor
public class HinhAnhRestController {
    private final HinhAnhService hinhAnhService;

    @GetMapping("/getAll")
    public List<HinhAnhDTO> getAllDanhMuc() {
        return hinhAnhService.getAllHinhAnh();
    }

}
