package com.example.datn.Repository;

import com.example.datn.entity.TrangThaiHDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@Repository
public interface TrangThaiHDRepository extends JpaRepository<TrangThaiHDEntity, UUID> {
}
