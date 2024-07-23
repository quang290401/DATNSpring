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
@Table(name = "GioHangChiTiet")
public class GioHangChiTietEntity implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "gioHang_id")
    private GioHangEntity gioHang;
    @Id
    @ManyToOne
    @JoinColumn(name = "sanPhamChitiet_id")
    private SanPhamChiTietEntity sanPhamChiTiet;
    @Column(name = "soLuong", length = 10, nullable = false)
    private int soLuong;
    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "updateDate")
    private LocalDate updateDate;
    @Column(name = "trangThai", length = 50, nullable = false, unique = true)
    private int trangThai;

}
