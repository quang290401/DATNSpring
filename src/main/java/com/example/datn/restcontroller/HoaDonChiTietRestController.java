package com.example.datn.restcontroller;

import com.example.datn.dto.GioHangChiTietDTO;
import com.example.datn.dto.HoaDonCHiTietCrud;
import com.example.datn.dto.HoaDonChiTietDTO;
import com.example.datn.service.HoaDonChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/HDCT")
public class HoaDonChiTietRestController {
    private final HoaDonChiTietService hoaDonChiTietService;
    @PostMapping("/{idUser}/{idVoucher}")
    public HoaDonCHiTietCrud addHoaDonCT (@PathVariable UUID idUser, @PathVariable UUID idVoucher) {
        return hoaDonChiTietService.addHoaDonCT(idUser,idVoucher);
    }
    @GetMapping("/{idUser}")
    public List<HoaDonChiTietDTO> getALLHDCTByIdUser (@PathVariable UUID idUser) {
        return hoaDonChiTietService.getALlHoaDonCTByIdUser(idUser);
    }
}
