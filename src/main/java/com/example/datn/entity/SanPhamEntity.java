package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name = "SanPham")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamEntity extends SuperEntity{

    @Column(name = "tenSanPham", length = 150, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String tenSanPham;

    @Column(name = "trangThai", length = 10, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    private int trangThai;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();



}
