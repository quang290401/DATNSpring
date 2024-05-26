package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "VouCher")
public class  VouCherEntity extends SuperEntity {
    @Column(name = "ma", length = 150, nullable = false, unique = true)
    private String ma;

    @Column(name = "ten", length = 150, nullable = false, unique = true)
    private String ten;
    @Column(name = "phanTramGiam", length = 10, nullable = false, unique = true)
    private int phanTramGiam;
    @Column(name = "ngayBatDau", length = 30, nullable = false, unique = true)
    private LocalDate ngayBatDau;
    @Column(name = "ngayKetThuc", length = 30, nullable = false, unique = true)
    private LocalDate ngayKetThuc;
    @Column(name = "trangThai", length = 50, nullable = false, unique = true)
    private int trangThai;
    @JsonIgnore
    @OneToMany(mappedBy = "vouCher")
    private List<HoaDonEntity> hoaDons = new ArrayList<HoaDonEntity>();

}
