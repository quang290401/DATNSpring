package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "thanhTien", length = 20, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1000, message = "Gia tien phai hon 1000 dong!")
    private BigDecimal tongTien;
    @Column(name = "ngayThanhToan", length = 120, nullable = false)
    private LocalDate ngayThanhToan;
    @Column(name = "trangThai", length = 20, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    private int trangThai;
    @ManyToOne
    @JoinColumn(name  = "vouCher_id")
    private VouCherEntity vouCher;
    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon")
    private List<HoaDonChiTietEntity> hoaDonChiTiets = new ArrayList<HoaDonChiTietEntity>();
}
