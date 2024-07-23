package com.example.datn.dto;


import com.example.datn.entity.KichCoEntity;
import com.example.datn.entity.MauSacEntity;
import com.example.datn.entity.SanPhamEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonChiTietDTO {
    private HoaDonDTO hoaDon;

    private SanPhamChiTietDTO sanPhamChiTiet;

    private int soLuong;

    private BigDecimal thanhTien;

    private LocalDate createDate;


    private LocalDate updateDate;

}
