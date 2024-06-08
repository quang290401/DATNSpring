package com.example.datn.Repository;

import com.example.datn.entity.DanhMucEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DanhMucRepository extends JpaRepository<DanhMucEntity, UUID> {
    Page<DanhMucEntity> findAll(Pageable pageable);
}
