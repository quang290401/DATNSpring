package com.example.datn.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "hoa_don_chitiet")
public class HoaDonChiTietEntity implements Serializable {

    @EmbeddedId
    private HoaDonChiTietPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("hoaDonId")
    @JoinColumn(name = "hoa_don_id", referencedColumnName = "id")
    private HoaDonEntity hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sanPhamChiTietId")
    @JoinColumn(name = "san_pham_chitiet_id", referencedColumnName = "id")
    private SanPhamChiTietEntity sanPhamChiTiet;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "thanh_tien")
    private BigDecimal thanhTien;

    @Column(name = "create_date")
    private LocalDate createDate;

    // Constructors, getters, setters, and other methods
}
