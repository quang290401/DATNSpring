package com.example.datn.controller;

import com.example.datn.Repository.ChiTietSPRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class SanPhamCTController {
    private final ChiTietSPRepository chiTietSPRepository;

    public SanPhamCTController(ChiTietSPRepository chiTietSPRepository) {
        this.chiTietSPRepository = chiTietSPRepository;
    }

//    @GetMapping("/getColorByShoeName/{tenSanPham}")
//    public ResponseEntity<List<String>> getColorByShoeName(@PathVariable String tenSanPham) {
//        List<String> mauSac = chiTietSPRepository.findColorByShoeName(tenSanPham);
//        if (mauSac != null && !mauSac.isEmpty()) {
//            return ResponseEntity.ok(mauSac);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @GetMapping("/getSizesByColor/{mauSac}/{tenSanPham}")
//    public ResponseEntity<List<String>> getSizesByColor(@PathVariable String mauSac , @PathVariable  String tenSanPham) {
//        List<String> kichCoList = chiTietSPRepository.findSizeByTenVaMauSac(mauSac,tenSanPham);
//        if (kichCoList != null && !kichCoList.isEmpty()) {
//            return ResponseEntity.ok(kichCoList);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @GetMapping("/getQuantityBySize/{kichCo}")
//    public ResponseEntity<List<Integer>> getQuantityBySize(@PathVariable String kichCo) {
//        List<Integer> soLuong = chiTietSPRepository.findQuantityBySize(kichCo);
//        if (soLuong != null && !soLuong.isEmpty()) {
//            return ResponseEntity.ok(soLuong);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
