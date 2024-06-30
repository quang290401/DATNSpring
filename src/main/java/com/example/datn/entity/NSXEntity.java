package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "NSX")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSXEntity extends SuperEntity {

    @Column(name = "ten", length = 150, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String ten;

    @Column(name = "trangThai", length = 10, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    private int trangThai;
    @JsonIgnore
    @OneToMany(mappedBy = "nsx")
    private List<SanPhamChiTietEntity> sanPhamChiTiets = new ArrayList<SanPhamChiTietEntity>();

}
