package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HoaDon")
public class HoaDonEntity extends SuperEntity {
    @ManyToOne
    @JoinColumn(name  = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name  = "trangThaiHD_id")
    private TrangThaiHDEntity trangThaiHD;
    @Column(name = "thanhTien", length = 20, nullable = false, unique = false)
    private BigDecimal tongTien;
    @Column(name = "ngayThanhToan", length = 120, nullable = false, unique = false)
    private LocalDate ngayThanhToan;
    @Column(name = "trangThai", length = 20, nullable = false, unique = false)
    private int trangThai;
    @ManyToOne
    @JoinColumn(name  = "vouCher_id")
    private VouCherEntity vouCher;
    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTietEntity> hoaDonChiTiets = new ArrayList<HoaDonChiTietEntity>();
}
