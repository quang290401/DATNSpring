package com.example.datn.repository;

import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTietEntity, UUID> {

    @Query("SELECT s FROM SanPhamChiTietEntity s ORDER BY s.createDate DESC")
    List<SanPhamChiTietEntity> findTop4SanPhamChiTiet(Pageable pageable);

}
