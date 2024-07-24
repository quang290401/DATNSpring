package com.example.datn.restcontroller;

import com.example.datn.common.Appcontants;
import com.example.datn.dto.GioHangChiTietCrud;
import com.example.datn.dto.SanPhamChiTietCrud;
import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.dto.SanPhamChiTietFiterDTO;
import com.example.datn.service.SanPhamChiTietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/SPCT")
public class SanPhamChiTietRestController {
    private final SanPhamChiTietService sanPhamChiTietService;
    @PostMapping()
    public ResponseEntity<?> addSanPhamChiTiet(@RequestBody SanPhamChiTietCrud sanPhamChiTietCrud) {
        SanPhamChiTietCrud sanPhamChiTietCrudSave = sanPhamChiTietService.addSanPhamChiTiet(sanPhamChiTietCrud);
        return ResponseEntity.ok(sanPhamChiTietCrudSave);

    }
    @PutMapping()
    public ResponseEntity<?> UpdateSanPhamChiTiet(@RequestBody SanPhamChiTietCrud sanPhamChiTietCrud) {
        SanPhamChiTietCrud sanPhamChiTietCrudSave = sanPhamChiTietService.upDateSanPhamChiTiet(sanPhamChiTietCrud);
        return ResponseEntity.ok(sanPhamChiTietCrudSave);

    }

    @GetMapping()
    public Page<SanPhamChiTietDTO> getAllProducts (
            @RequestParam(value = "pageNo", defaultValue = Appcontants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Appcontants.DEFAULT_TOTAL_NUMBER, required = false) int pageSize
    , @Valid SanPhamChiTietFiterDTO filterForm) {

        return sanPhamChiTietService.getAllSanPhamChiTiet(pageNo, pageSize,filterForm);
    }
    @GetMapping("/idSP")
    public Page<SanPhamChiTietDTO> getAllProductsByidSP (
            @RequestParam(value = "pageNo", defaultValue = Appcontants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = Appcontants.DEFAULT_TOTAL_NUMBER, required = false) int pageSize
            , @Valid UUID idSP) {

        return sanPhamChiTietService.getAllSanPhamChiTietBYidSP(idSP,pageNo, pageSize);
    }
    @GetMapping("/Detail/{id}")
    public  SanPhamChiTietDTO getById(@PathVariable UUID id){
        return sanPhamChiTietService.findById(id);
    }

    @GetMapping("/top4")
    public List<SanPhamChiTietDTO> getForSPCT() {
        Pageable topFour = PageRequest.of(0, 4);
        return sanPhamChiTietService.GetForSP(topFour);
    }

}
