package com.example.datn.dto;

import com.example.datn.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SanPhamChiTietDTO extends SuperDTO {

    private BigDecimal giaSanPham;

    private int soLuong;

    private String trongLuong;

    private int gioiTinh;

    private String moTa;


    private String sanPham;

    private String mauSac;

    private String kichCo;

    private String nsx;

    private String chatLieu;

    private String hinhAnh;

    private String danhMuc;

    private String baoHanh;


}
