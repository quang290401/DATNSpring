package com.example.datn.service;

import com.example.datn.dto.TrangThaiHoaDonDTO;

import java.util.UUID;

public interface TrangThaiHdService {
    TrangThaiHoaDonDTO findByID(UUID idTrangThaiHd);
}
