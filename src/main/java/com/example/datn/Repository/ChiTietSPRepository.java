package com.example.datn.Repository;

import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.entity.SanPhamChiTietPK;
import com.example.datn.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Repository
public interface ChiTietSPRepository extends JpaRepository<SanPhamChiTietEntity, UUID> {
    Optional<UserEntity> findById(SanPhamChiTietPK sanPhamChiTietPK);
    @Query(
            value = """
//    select san_pham.ten_san_pham, kich_co.ten_kich_co,mau_sac.ten, so_luong  from san_pham_chi_tiet
//                join kich_co on san_pham_chi_tiet.id = kich_co.id
//                join san_pham on kich_co.id = san_pham.id
//                join mau_sac on san_pham.id = mau_sac.id
//    """,
            nativeQuery = true)
    Collection<Object> findAllActiveUsersNative();

    @Query("SELECT m.ten AS tenMauSac, k.tenKichCo AS tenKichCo " +
            "FROM SanPhamChiTietEntity s " +
            "JOIN MauSacEntity m ON s.mauSac.id = m.id " +
            "JOIN KichCoEntity k ON s.kichCo.id = k.id " +
            "WHERE s.id = :sanPhamChiTietId")
    Map<String, Object> findByNameSP(@PathVariable("sanPhamId") UUID sanPhamChiTietId);

    @Query("SELECT DISTINCT m.ten FROM SanPhamChiTietEntity sp " +
            "JOIN sp.mauSac m " +
            "WHERE sp.sanPham.tenSanPham = :tenSanPham")
    List<String> findColorByShoeName(@Param("tenSanPham") String tenSanPham);

    @Query("SELECT DISTINCT k.tenKichCo FROM SanPhamChiTietEntity spct " +
            "JOIN spct.sanPham sp " +
            "JOIN spct.kichCo k " +
            "WHERE spct.sanPham.id = :sanPhamId " +
            "AND spct.mauSac.id = :mauSacId")
    List<String> findSizeByTenVaMauSac(@Param("sanPhamId") String sanPhamId, @Param("mauSacId") String mauSacId);

    @Query("SELECT sp.soLuong FROM SanPhamChiTietEntity sp " +
            "WHERE sp.kichCo.tenKichCo = :kichCo")
    List<Integer> findQuantityBySize(@Param("kichCo") String kichCo);

}
