package com.example.datn.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietPK implements Serializable {
    @Column(name = "hoa_don_id")
    private UUID hoaDonId;

    @Column(name = "san_pham_chitiet_id")
    private UUID sanPhamChiTietId;
}
