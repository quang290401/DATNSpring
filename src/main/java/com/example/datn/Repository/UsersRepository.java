package com.example.datn.repository;

import com.example.datn.entity.SanPhamEntity;
import com.example.datn.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByTaiKhoan(String taiKhoan);

}
