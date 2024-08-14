package com.example.datn.Repository;

import com.example.datn.dto.TrangThaiHoaDonDTO;
import com.example.datn.entity.HoaDonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDonEntity, UUID> {
    @Query("SELECT g FROM HoaDonEntity g WHERE g.user.id = :userId AND g.trangThaiHD.trangThai IN ('1','2','3','5') ORDER BY g.createDate DESC")
    List<HoaDonEntity> findByHoaByIdUser(@Param("userId") UUID userId);
    @Query("SELECT g FROM HoaDonEntity g WHERE g.user.id = :userId AND g.trangThaiHD.trangThai IN ('4') ORDER BY g.createDate DESC")
    List<HoaDonEntity> findByHoaByIdUserHuy(@Param("userId") UUID userId);

    @Query("SELECT new com.example.datn.dto.TrangThaiHoaDonDTO(hd.id, tt.trangThai) " +
            "FROM HoaDonEntity hd " +
            "JOIN hd.trangThaiHD tt " +
            "WHERE hd.id = :hoaDonId")
    TrangThaiHoaDonDTO findTrangThaiByHoaDonId(@Param("hoaDonId") UUID hoaDonId);
    @Query("SELECT h FROM HoaDonEntity h JOIN h.trangThaiHD t WHERE t.trangThai IN ('2', '3')")
    List<HoaDonEntity> findHoaDonsByTrangThai();
    @Query("SELECT h FROM HoaDonEntity h JOIN h.trangThaiHD t WHERE t.trangThai =('4')")
    List<HoaDonEntity> findTTByHoaDon();
    @Modifying
    @Query("UPDATE HoaDonEntity h SET h.trangThaiHD = (SELECT t FROM TrangThaiHDEntity t WHERE t.trangThai = '4') WHERE h.id = :idHoaDon")
    void updateTrangThaiHd(@Param("idHoaDon") UUID idHoaDon);



//        @Query("SELECT hd.trangThaiHD.ten FROM HoaDonEntity hd WHERE hd.id = :hoaDonId")
//        Integer findTrangThaiById(@PathVariable("hoaDonId") UUID hoaDonId);
//        @Query(value = "WITH Months AS (\n" +
//                "    SELECT 1 AS MonthNumber, N'Tháng1' AS MonthName\n" +
//                "    UNION ALL SELECT 2, N'Tháng2'\n" +
//                "    UNION ALL SELECT 3,N'Tháng3'\n" +
//                "    UNION ALL SELECT 4, N'Tháng4'\n" +
//                "    UNION ALL SELECT 5, N'Tháng5'\n" +
//                "    UNION ALL SELECT 6, N'Tháng6'\n" +
//                "    UNION ALL SELECT 7, N'Tháng7'\n" +
//                "    UNION ALL SELECT 8, N'Tháng8'\n" +
//                "    UNION ALL SELECT 9,N'Tháng9'\n" +
//                "    UNION ALL SELECT 10, N'Tháng10'\n" +
//                "    UNION ALL SELECT 11, N'Tháng11'\n" +
//                "    UNION ALL SELECT 12, N'Tháng12'\n" +
//                ")\n" +
//                "SELECT \n" +
//                "    m.MonthName,\n" +
//                "    ISNULL(SUM(hd.thanh_tien), 0) AS totalSales\n" +
//                "FROM \n" +
//                "    Months m\n" +
//                "LEFT JOIN \n" +
//                "    hoa_don hd ON MONTH(hd.ngay_thanh_toan) = m.MonthNumber\n" +
//                "LEFT JOIN \n" +
//                "    hoa_don_chitiet hdct ON hd.id = hdct.hoa_don_id\n" +
//                "WHERE \n" +
//                "    YEAR(hd.ngay_thanh_toan) = YEAR(GETDATE()) -- Thay đổi năm nếu cần\n" +
//                "    AND hd.ngay_thanh_toan >= DATEADD(MONTH, -12, GETDATE()) -- Lọc trong 12 tháng gần nhất\n" +
//                "GROUP BY \n" +
//                "    m.MonthName, \n" +
//                "    m.MonthNumber\n" +
//                "ORDER BY \n" +
//                "    m.MonthNumber;\n", nativeQuery = true)
//            List<Object[]> thongKeTheoThang();
//        @Query(value = "SELECT TOP 5 " +
//                "    sp.anh_san_pham, " +
//                "    sp.ten_san_pham, " +
//                "    SUM(hdct.so_luong) AS soLuongDaBan " +
//                "FROM " +
//                "    san_pham sp " +
//                "JOIN " +
//                "    san_pham_chi_tiet spct ON sp.id = spct.sanpham_id " +
//                "JOIN " +
//                "    hoa_don_chitiet hdct ON spct.id = hdct.san_pham_chitiet_id " +
//                "GROUP BY " +
//                "    sp.ten_san_pham, sp.anh_san_pham " +
//                "ORDER BY " +
//                "    soLuongDaBan DESC", nativeQuery = true)
//        List<Object[]> sanPhamBanChay();

    }


