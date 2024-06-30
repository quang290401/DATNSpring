package com.example.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DiaChi")
public class DiaChiEntity extends SuperEntity {
    @Column(name = "diaChi", length = 120, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String diaChi;
    @Column(name = "thanhPho", length = 150, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String thanhPho;
    @Column(name = "quocGia", length = 150, nullable = false)
    @NotBlank(message = "Khong duoc de trong!")
    private String quocGia;
    @JsonIgnore
    @OneToMany(mappedBy = "diaChi")
    private List<UserEntity> userEntities = new ArrayList<UserEntity>();
    @Column(name = "trangThai", length = 10, nullable = false)
    private int trangThai;
}
