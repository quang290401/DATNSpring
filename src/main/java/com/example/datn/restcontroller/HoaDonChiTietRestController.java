package com.example.datn.restcontroller;

import com.example.datn.dto.GioHangChiTietDTO;
import com.example.datn.dto.HoaDonCHiTietCrud;
import com.example.datn.dto.HoaDonChiTietDTO;
import com.example.datn.dto.HoaDonDTO;
import com.example.datn.service.HoaDonChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/HDCT")
public class HoaDonChiTietRestController {
    private final HoaDonChiTietService hoaDonChiTietService;
    @PostMapping("/{idUser}/{idVoucher}/{idTrangThaiHD}/{tongTien}")
    public HoaDonCHiTietCrud addHoaDonCT (@PathVariable UUID idUser, @PathVariable UUID idVoucher,
                                          @PathVariable UUID idTrangThaiHD,
                                          @PathVariable BigDecimal tongTien) {
        return hoaDonChiTietService.addHoaDonCT(idUser,idVoucher,idTrangThaiHD,tongTien);
    }
    @GetMapping("/{idUser}")
    public List<HoaDonDTO> getALLHoaDonByIdUser (@PathVariable UUID idUser) {
        return hoaDonChiTietService.getAllHoaDonByIdUser(idUser);
    }
    @GetMapping("/GetALL/{idHoaDon}")
    public List<HoaDonChiTietDTO> getALLHoaDonByIdHoaDon (@PathVariable UUID idHoaDon) {
        return hoaDonChiTietService.getALlHoaDonCTByIdHoaDon(idHoaDon);
    }
}
