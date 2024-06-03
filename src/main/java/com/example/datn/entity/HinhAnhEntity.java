package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HinhAnh")
public class HinhAnhEntity extends SuperEntity {
    @Column(name = "ma", length = 150, nullable = false, unique = true)
    private String ma;

    @Column(name = "ten", length = 150, nullable = false, unique = false)
    private String ten;
    @Column(name = "duongDan", length = 150, nullable = false, unique = true)
    private String duongDan;

    @Column(name = "trangThai", length = 10, nullable = false, unique = false)
    private int trangThai;
    @JsonIgnore
    @OneToMany(mappedBy = "hinhAnh")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();

}
