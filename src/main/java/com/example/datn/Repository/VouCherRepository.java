package com.example.datn.Repository;

import com.example.datn.entity.VouCherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VouCherRepository extends JpaRepository<VouCherEntity, UUID> {
}
