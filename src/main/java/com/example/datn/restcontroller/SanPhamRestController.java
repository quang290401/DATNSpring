package com.example.datn.restcontroller;

import com.example.datn.common.Appcontants;
import com.example.datn.dto.SanPhamDTO;
import com.example.datn.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sp")
public class SanPhamRestController {
    private final SanPhamService service;

    @GetMapping()
    public Page<SanPhamDTO> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = Appcontants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Appcontants.DEFAULT_TOTAL_NUMBER, required = false) int pageSize
    ) {

        return service.getAllSanPham(pageNo, pageSize);
    }
    @PostMapping()
    public SanPhamDTO addSanPham(@RequestBody SanPhamDTO sanPhamDTO){
    return service.addSanPham(sanPhamDTO);

    }
    @PutMapping()
    public SanPhamDTO upDateSanPham(@RequestBody SanPhamDTO sanPhamDTO){
        return service.upDateSanPham(sanPhamDTO);

    }
}
