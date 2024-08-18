package com.example.datn.Repository;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.entity.UserEntity;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface  UsersRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE u.taiKhoan = :taiKhoan AND u.trangThai = 1")
    Optional<UserEntity> findByTaiKhoan(@Param("taiKhoan") String taiKhoan);
    @Modifying
    @Query("UPDATE UserEntity u SET u.trangThai =0 WHERE u.id = :userId")
    void updateUserKhoa( @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE UserEntity u SET u.trangThai =1 WHERE u.id = :userId")
    void updateUserMo( @Param("userId") UUID userId);

    Optional<UserEntity> findBySdt(String sdt);


}
