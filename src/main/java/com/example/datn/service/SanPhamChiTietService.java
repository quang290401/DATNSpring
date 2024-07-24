package com.example.datn.service;

import com.example.datn.dto.GioHangChiTietCrud;
import com.example.datn.dto.SanPhamChiTietCrud;
import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.dto.SanPhamChiTietFiterDTO;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SanPhamChiTietService {
    Page<SanPhamChiTietDTO> getAllSanPhamChiTiet(Integer totalPage, Integer totalItem, SanPhamChiTietFiterDTO form);
    Page<SanPhamChiTietDTO> getAllSanPhamChiTietBYidSP(UUID idSP,Integer totalPage, Integer totalItem);
    SanPhamChiTietCrud addSanPhamChiTiet(SanPhamChiTietCrud sanPhamChiTietCrud);
    SanPhamChiTietCrud upDateSanPhamChiTiet(SanPhamChiTietCrud sanPhamChiTietCrud);
    SanPhamChiTietDTO findById(UUID id);
    List<SanPhamChiTietDTO> GetForSP(Pageable pageable);
}
