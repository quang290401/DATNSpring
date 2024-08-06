package com.example.datn.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDonChiTietDTO {
    private HoaDonDTO hoaDon;

    private SanPhamChiTietDTO sanPhamChiTiet;

    private int soLuong;

    private BigDecimal thanhTien;

    private LocalDate createDate;


    private LocalDate updateDate;

    public HoaDonChiTietDTO(UUID id, String tenSanPham, String ten, String tenKichCo, int soLuong, BigDecimal giaSanPham, BigDecimal thanhTien) {
    }
}
