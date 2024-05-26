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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="Users")
public class UserEntity extends SuperEntity{
    @Column(name = "taiKhoan", length = 50, nullable = false, unique = true)
    private String taiKhoan;
    @Column(name = "ten", length =150, nullable = false, unique = true)
    private String ten;
    @Column(name = "tenDem", length = 150, nullable = false, unique = true)
    private String tenDem;
    @Column(name = "ho", length = 150, nullable = false, unique = true)
    private String ho;
    @Column(name = "sdt", length = 50, nullable = false, unique = true)
    private String sdt;
    @Column(name = "matKhau", length = 30, nullable = false, unique = true)
    private String matKhau;
    @Column(name = "ngaySinh", length = 30, nullable = false, unique = true)
    private LocalDate ngaySinh;
    @Column(name = "gioiTinh", length = 10, nullable = false, unique = true)
    private int gioiTinh;
    @Column(name = "trangThai", length = 10, nullable = false, unique = true)
    private int trangThai;
    @ManyToOne
    @JoinColumn(name  = "vaiTro_id")
    private VaiTroEntity vaiTro;
    @ManyToOne
    @JoinColumn(name  = "diaChi_id")
    private DiaChiEntity diaChi;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<GioHangEntity> gioHangs = new ArrayList<GioHangEntity>();
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<HoaDonEntity> hoaDons = new ArrayList<HoaDonEntity>();


}
