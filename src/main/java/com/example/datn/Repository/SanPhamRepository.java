package com.example.datn.Repository;

import com.example.datn.entity.SanPhamEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface SanPhamRepository extends JpaRepository<SanPhamEntity, UUID>, JpaSpecificationExecutor<SanPhamEntity> {
    @Query("SELECT sp FROM SanPhamEntity sp WHERE sp.trangThai = :trangThai")
    List<SanPhamEntity> findByTrangThai(@Param("trangThai") String trangThai, Pageable pageable);
}
