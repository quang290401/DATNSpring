package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "kichCo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KichCoEntity extends SuperEntity {

    @Column(name = "tenKichCo", length = 120, nullable = false, unique = true)
    private String tenKichCo;
    @Column(name = "doDai", length = 150, nullable = false, unique = false)
    private String doDai;
    @JsonIgnore
    @OneToMany(mappedBy = "kichCo")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();
}
