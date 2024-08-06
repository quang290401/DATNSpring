package com.example.datn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamBanChayDTO {
    private String hinhAnhDuongDan;
    private String tenSanPham;
    private Integer soLuongDaBan;
}
