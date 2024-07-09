package com.example.datn.Repository;

import com.example.datn.entity.SanPhamChiTietEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTietEntity, UUID> {

    @Query("SELECT s FROM SanPhamChiTietEntity s ORDER BY s.createDate DESC")
    List<SanPhamChiTietEntity> findTop4SanPhamChiTiet(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTietEntity sp SET sp.soLuong = sp.soLuong - :soLuong WHERE sp.id = :id")
    void updateSoLuong(UUID id, int soLuong);

}
