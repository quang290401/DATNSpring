package com.example.datn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HoaDonChiTiet")
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

    @ManyToOne
    @JoinColumn(name  = "user_id")
    private UserEntity user;
    @Column(name = "soLuong", length = 10, nullable = false)
    private int soLuong;
    @Column(name = "thanhTien", length = 70, nullable = false)
    private BigDecimal thanhTien;
    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "updateDate")
    private LocalDate updateDate;
    // Constructors, getters, setters, and other methods
}
