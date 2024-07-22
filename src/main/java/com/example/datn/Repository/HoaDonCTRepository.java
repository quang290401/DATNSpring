package com.example.datn.Repository;

import com.example.datn.entity.HoaDonChiTietEntity;
import com.example.datn.entity.HoaDonChiTietPK;
import com.example.datn.entity.HoaDonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoaDonCTRepository extends JpaRepository<HoaDonChiTietEntity, HoaDonChiTietPK> {
    List<HoaDonChiTietEntity> findByHoaDon_Id(UUID hoaDonId);
    @Query("SELECT COALESCE(SUM(ct.soLuong), 0) FROM HoaDonChiTietEntity ct WHERE ct.id.hoaDonId = :hoaDonId AND ct.id.sanPhamChiTietId = :sanPhamChiTietId")
    int calculateTotalSoLuongByHoaDonIdAndSanPhamChiTietId(@Param("hoaDonId") UUID hoaDonId, @Param("sanPhamChiTietId") UUID sanPhamChiTietId);

    Optional<HoaDonChiTietEntity> findByHoaDonIdAndSanPhamChiTietId(UUID hoaDonId, UUID sanPhamCTid);

    List<HoaDonChiTietEntity> findByHoaDonId(UUID hoaDonId);

    void deleteByHoaDonId(UUID hoaDonId);
}
