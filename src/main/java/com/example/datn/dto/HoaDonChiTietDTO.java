package com.example.datn.dto;

import com.example.datn.entity.HoaDonEntity;
import com.example.datn.entity.SanPhamChiTietEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HoaDonChiTietDTO {

    private HoaDonDTO hoaDon;

    private SanPhamChiTietDTO sanPhamChiTiet;

    private int soLuong;

    private BigDecimal thanhTien;

    private LocalDate createDate;


    private LocalDate updateDate;

}
