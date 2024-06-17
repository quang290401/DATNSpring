package com.example.datn.restcontroller;

import com.example.datn.dto.GioHangChiTietCrud;
import com.example.datn.dto.GioHangChiTietDTO;
import com.example.datn.service.GioHangChiTietService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/GHCT")
public class GioHangChiTietResController {
    private final GioHangChiTietService gioHangChiTietService;

    @GetMapping("/user/{idUser}")
    public List<GioHangChiTietDTO>  getAllProducts (@PathVariable UUID idUser) {
        return gioHangChiTietService.getALLGioHangChiTiet(idUser);
    }
    @PostMapping()
    public ResponseEntity<GioHangChiTietCrud> addGioHangChiTiet(@RequestBody GioHangChiTietCrud gioHangChiTietDTO) {
        GioHangChiTietCrud savedGioHangChiTiet = gioHangChiTietService.addGioHangCT( gioHangChiTietDTO);
        return ResponseEntity.ok(savedGioHangChiTiet);
    }
   @DeleteMapping("{id}")
    public void DeleteGioHangCT(@PathVariable UUID id){
        gioHangChiTietService.deleteGioHangCT(id);
   }
}
