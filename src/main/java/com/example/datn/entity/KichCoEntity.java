package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(name = "tenKichCo", length = 120, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String tenKichCo;
    @Column(name = "doDai", length = 150, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    private String doDai;
    @JsonIgnore
    @OneToMany(mappedBy = "kichCo")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();
}
