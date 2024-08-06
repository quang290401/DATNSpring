package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "giaSanPham", length = 150, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1000, message = "Gia tien phai hon 1000 dong!")
    private BigDecimal giaSanPham;
    @Column(name = "soLuong", length = 50, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1, message = "So luong phai lon hon 1!")
    private int soLuong;
    @Column(name = "trongLuong", length = 150, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1, message = "Trong luong phai lon hon 1!")
    private String trongLuong;
    @Column(name = "gioiTinh", length = 10, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private int gioiTinh;
    @Column(name = "moTa", length = 150, nullable = false)
    private String moTa;
    @Column(name = "trangThai", length = 150, nullable = false)
    private String trangThai;

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
