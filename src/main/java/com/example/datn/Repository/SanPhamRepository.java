package com.example.datn.Repository;

import com.example.datn.entity.SanPhamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SanPhamRepository extends JpaRepository<SanPhamEntity, UUID> {
}
