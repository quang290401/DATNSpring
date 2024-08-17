package com.example.datn.Repository;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface  UsersRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE u.taiKhoan = :taiKhoan AND u.trangThai = 1")
    Optional<UserEntity> findByTaiKhoan(@Param("taiKhoan") String taiKhoan);

    Optional<UserEntity> findBySdt(String sdt);


}
