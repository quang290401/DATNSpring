package com.example.datn.Repository;

import com.example.datn.entity.GioHangChiTietEntity;
import com.example.datn.entity.HoaDonChiTietEntity;
import com.example.datn.entity.HoaDonEntity;
import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTietEntity, SanPhamChiTietEntity> {
    @Query("SELECT g FROM HoaDonChiTietEntity g WHERE g.hoaDon.id = :idHoaDon")
    List<HoaDonChiTietEntity>  findByHoaDonChiTietByIdHoaDon(@Param("idHoaDon") UUID idHoaDon);



}
