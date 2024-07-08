package com.example.datn.Repository;

import com.example.datn.entity.SanPhamChiTietEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChiTietSPRepository extends JpaRepository<SanPhamChiTietEntity, UUID> {

//    @Query(
//            value = """
//    select san_pham.ten_san_pham, kich_co.ten_kich_co,mau_sac.ten, so_luong  from san_pham_chi_tiet
//                join kich_co on san_pham_chi_tiet.id = kich_co.id
//                join san_pham on kich_co.id = san_pham.id
//                join mau_sac on san_pham.id = mau_sac.id
//    """,
//            nativeQuery = true)
//    Collection<Object> findAllActiveUsersNative();
    @Query(value = "select ct from SanPhamChiTietEntity ct join SanPhamEntity sp on ct.sanPham.id = sp.id where sp.tenSanPham like %:ten%")
    List<SanPhamChiTietEntity> getData(@Param("ten") String ten);
//    List<SanPhamChiTietEntity> getAllBySanPham_TenSanPham( String ten);

    @Query(value = "select ct from SanPhamChiTietEntity ct join SanPhamEntity sp on ct.sanPham.id = sp.id")
    Page<SanPhamChiTietEntity> pageAll(Pageable page);

    @Query(value = "select ct from SanPhamChiTietEntity ct join SanPhamEntity sp on ct.sanPham.id = sp.id where sp.tenSanPham like %:ten%")
    Page<SanPhamChiTietEntity> pageSearch(Pageable page, @Param("ten") String ten);


}
