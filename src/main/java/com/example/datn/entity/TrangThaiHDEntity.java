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
@Table(name = "TrangThaiHD")
public class TrangThaiHDEntity extends SuperEntity {
    @Column(name = "ten", length = 120, nullable = false, unique = true)
    private String ten;
    @Column(name = "trangThai", length = 120, nullable = false, unique = false)
    private String trangThai;

    @JsonIgnore
    @OneToMany(mappedBy = "trangThaiHD")
    private List<HoaDonEntity> hoaDons = new ArrayList<HoaDonEntity>();
}
