package com.example.datn.service;

import com.example.datn.dto.SanPhamChiTietDTO;
import org.springframework.data.domain.Page;

public interface SanPhamChiTietService {
    Page<SanPhamChiTietDTO> getAllSanPhamChiTiet(Integer totalPage, Integer totalItem);
}
