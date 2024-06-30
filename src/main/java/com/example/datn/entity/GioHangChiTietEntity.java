package com.example.datn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTietEntity implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "gioHang_id")
    private GioHangEntity gioHang;
    @Id
    @ManyToOne
    @JoinColumn(name = "sanPhamChitiet_id")
    private SanPhamChiTietEntity sanPhamChiTiet;

    @Column(name = "soLuong", length = 10, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1, message = "So luong phai lon hon 1!")
    private int soLuong;

    @Column(name = "thanhTien", length = 70, nullable = false)
    @NotNull(message = "Khong duoc de trong!")
    @Min(value = 1000, message = "Gia tien phai hon 1000 dong!")
    private BigDecimal thanhTien;

    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "updateDate")
    private LocalDate updateDate;

    @Column(name = "trangThai", length = 50, nullable = false, unique = true)
    private int trangThai;

}
