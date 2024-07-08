package com.example.datn.service;


import com.example.datn.dto.HoaDonCHiTietCrud;
import com.example.datn.dto.HoaDonChiTietDTO;
import com.example.datn.dto.HoaDonDTO;
import java.util.List;
import java.util.UUID;

public interface HoaDonChiTietService {
   HoaDonCHiTietCrud addHoaDonCT(UUID idUser,UUID idVoucher,UUID idTrangThaiHD);
   List<HoaDonChiTietDTO> getALlHoaDonCTByIdHoaDon(UUID hoaDon);
   List<HoaDonDTO> getAllHoaDonByIdUser(UUID idUser);
}
