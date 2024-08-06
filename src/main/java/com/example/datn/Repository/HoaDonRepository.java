package com.example.datn.Repository;
import com.example.datn.entity.HoaDonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDonEntity, UUID> {
    @Query("SELECT g FROM HoaDonEntity g WHERE g.user.id = :userId ORDER BY g.createDate DESC")
    List<HoaDonEntity> findByHoaByIdUser(@Param("userId") UUID userId);
    @Query("SELECT tt.ten AS trangThai " +
            "FROM HoaDonEntity hd " +
            "JOIN hd.trangThaiHD tt " +
            "WHERE hd.id = :hoaDonId")
    Map<String, String> findTrangThaiById(@Param("hoaDonId") UUID hoaDonId);



    }


