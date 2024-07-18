package com.example.datn.Repository;

import com.example.datn.entity.SanPhamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SanPhamRepository extends JpaRepository<SanPhamEntity, UUID> {
}
