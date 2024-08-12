package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="Users")
@Builder
public class UserEntity extends SuperEntity{


    @Column(name = "taiKhoan", length = 50, nullable = false)
    private String taiKhoan;
    @Column(name = "ten", length =150, nullable = false,columnDefinition = "NVARCHAR(255)")
    private String ten;
    @Column(name = "tenDem", length = 150, nullable = false,columnDefinition = "NVARCHAR(255)")
    private String tenDem;
    @Column(name = "ho", length = 150, nullable = false,columnDefinition = "NVARCHAR(255)")
    private String ho;
    @Column(name = "sdt", length = 50, nullable = true)
    private String sdt;
    @Column(name = "matKhau", length = 60, nullable = true)
    private String matKhau;
    @Column(name = "ngaySinh", length = 30, nullable = true)
    private LocalDate ngaySinh;
    @Column(name = "gioiTinh", length = 10, nullable = true)
    private int gioiTinh;
        @Column(name = "trangThai", length = 10, nullable = true)
    private int trangThai;
    @ManyToOne(fetch = FetchType.LAZY)
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
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<HoaDonChiTietEntity> hoaDonEntities = new ArrayList<HoaDonChiTietEntity>();

    public void setDiaChi(String s) {
    }

}
