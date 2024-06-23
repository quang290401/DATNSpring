package com.example.datn.service;


import com.example.datn.dto.HoaDonCHiTietCrud;
import com.example.datn.dto.HoaDonChiTietDTO;


import java.util.List;
import java.util.UUID;

public interface HoaDonChiTietService {
   HoaDonCHiTietCrud addHoaDonCT(UUID idUser,UUID idVoucher);
   List<HoaDonChiTietDTO> getALlHoaDonCTByIdUser(UUID idUser);
}
