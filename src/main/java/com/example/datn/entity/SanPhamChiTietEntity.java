package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "SanPhamChiTiet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietEntity extends SuperEntity{

    @Column(name = "giaSanPham", length = 150, nullable = false, unique = true)
    private BigDecimal giaSanPham;
    @Column(name = "soLuong", length = 50, nullable = false, unique = true)
    private int soLuong;
    @Column(name = "trongLuong", length = 150, nullable = false, unique = true)
    private String trongLuong;
    @Column(name = "gioiTinh", length = 10, nullable = false, unique = true)
    private int gioiTinh;
    @Column(name = "moTa", length = 150, nullable = false, unique = true)
    private String moTa;

    @ManyToOne
    @JoinColumn(name  = "sanpham_id")
    private SanPhamEntity sanPham;
    @ManyToOne
    @JoinColumn(name  = "mauSac_id")
    private MauSacEntity mauSac;
    @ManyToOne
    @JoinColumn(name  = "kichCo_id")
    private KichCoEntity kichCo;
    @ManyToOne
    @JoinColumn(name  = "NXS_id")
    private NSXEntity nsx;
    @ManyToOne
    @JoinColumn(name  = "chatLieu_id")
    private ChatLieuEntity chatLieu;
    @ManyToOne
    @JoinColumn(name  = "hinhAnh_id")
    private HinhAnhEntity hinhAnh;
    @ManyToOne
    @JoinColumn(name  = "danhMuc_id")
    private DanhMucEntity danhMuc;
    @ManyToOne
    @JoinColumn(name  = "baoHanh_id")
    private BaoHanhEntity baoHanh;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPhamChiTiet")
    private List<GioHangChiTietEntity> gioHangChiTietEntities = new ArrayList<GioHangChiTietEntity>();
    @JsonIgnore
    @OneToMany(mappedBy = "sanPhamChiTiet")
    private List<HoaDonChiTietEntity> hoaDonChiTiets = new ArrayList<HoaDonChiTietEntity>();




}
