package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MauSac")
public class MauSacEntity extends SuperEntity{

    @Column(name = "ten", length = 150, nullable = false)
    private String ten;

    @Column(name = "trangThai", length = 10, nullable = false)
    private int trangThai;
    @JsonIgnore
    @OneToMany(mappedBy = "mauSac")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();

}
