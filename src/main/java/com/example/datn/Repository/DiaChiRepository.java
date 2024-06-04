package com.example.datn.Repository;

import com.example.datn.entity.DiaChiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiaChiRepository extends JpaRepository<DiaChiEntity, UUID> {
}
