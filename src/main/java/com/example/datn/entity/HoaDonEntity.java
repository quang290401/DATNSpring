package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hoa_don")
public class HoaDonEntity extends SuperEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "trangThaiHD_id")
    private TrangThaiHDEntity trangThaiHD;

    @Column(name = "thanh_tien", nullable = false, precision = 20, scale = 2)
    private BigDecimal tongTien;
    @Column(name = "ngay_thanh_toan")
    private LocalDateTime  ngayThanhToan;

    @Column(name = "trang_thai", nullable = false)
    private int trangThai;

    @ManyToOne
    @JoinColumn(name = "vouCher_id")
    private VouCherEntity vouCher;

    @ManyToOne
    @JoinColumn(name = "sanphamchitiet_id")
    private SanPhamChiTietEntity sanPhamChiTiet;

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoaDonChiTietEntity> hoaDonChiTiets = new ArrayList<>();
}
