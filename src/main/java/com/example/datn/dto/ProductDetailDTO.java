package com.example.datn.dto;

import java.math.BigDecimal;
import java.util.UUID;

public  class ProductDetailDTO {
    private UUID sanPhamChiTietId;
    private String tenSanPham;
    private String mauSac;
    private String kichCo;
    private int soLuong;
    private BigDecimal giaBan;
    private BigDecimal thanhTien;

    // Constructor
    public ProductDetailDTO(UUID sanPhamChiTietId, String tenSanPham, String mauSac, String kichCo, int soLuong, BigDecimal giaBan, BigDecimal thanhTien) {
        this.sanPhamChiTietId = sanPhamChiTietId;
        this.tenSanPham = tenSanPham;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.thanhTien = thanhTien;
    }

    // Getters and Setters
    public UUID getSanPhamChiTietId() {
        return sanPhamChiTietId;
    }

    public void setSanPhamChiTietId(UUID sanPhamChiTietId) {
        this.sanPhamChiTietId = sanPhamChiTietId;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getKichCo() {
        return kichCo;
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
}