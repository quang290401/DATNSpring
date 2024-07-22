package com.example.datn.restcontroller;

import com.example.datn.dto.DanhMucDTO;
import com.example.datn.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/danhmuc")
@RequiredArgsConstructor
public class DanhMucRestController {


    private final DanhMucService danhMucService;

    @GetMapping("/getAll")
    public List<DanhMucDTO> getAllDanhMuc() {
        return danhMucService.getAllDanhMuc();
    }

}
