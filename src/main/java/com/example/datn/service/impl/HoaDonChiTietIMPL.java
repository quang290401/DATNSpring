package com.example.datn.service.impl;

import com.example.datn.Repository.*;
import com.example.datn.dto.*;
import com.example.datn.entity.*;
import com.example.datn.service.HoaDonChiTietService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoaDonChiTietIMPL implements HoaDonChiTietService {
    private final ModelMapper modelMapper;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final GioHangRepository gioHangRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final VouCherRepository vouCherRepository;
    private final UsersRepository usersRepository;
    private final TrangThaiHDRepository trangThaiHDRepository;

    @Override
    @Transactional
    public HoaDonCHiTietCrud addHoaDonCT(UUID idUser, UUID idVoucher, UUID idTrangThaiHD, BigDecimal tongtien) {
        try {
            Optional<VouCherEntity> vouCher = vouCherRepository.findById(idVoucher);
            Optional<UserEntity> user = usersRepository.findById(idUser);
            Optional<TrangThaiHDEntity> hdEntity = trangThaiHDRepository.findById(idTrangThaiHD);

            if (!vouCher.isPresent() || !user.isPresent() || !hdEntity.isPresent()) {
                throw new IllegalArgumentException("Voucher, User hoặc Trạng thái Hóa đơn không hợp lệ.");
            }

            UUID idHD = UUID.randomUUID();
            HoaDonEntity hoaDonEntity = new HoaDonEntity();
            hoaDonEntity.setId(idHD);
            hoaDonEntity.setUser(user.get());
            hoaDonEntity.setVouCher(vouCher.get());
            hoaDonEntity.setNgayThanhToan(LocalDate.from(LocalDateTime.now()));
            hoaDonEntity.setCreateDate(LocalDate.from(LocalDateTime.now()));
            hoaDonEntity.setTongTien(BigDecimal.valueOf(0));
            hoaDonEntity.setTrangThaiHD(hdEntity.get());
            hoaDonEntity = hoaDonRepository.save(hoaDonEntity);
            GioHangEntity gioHangEntity = gioHangRepository.findByUserId(idUser);
            List<GioHangChiTietEntity> gioHangChiTietEntities = gioHangChiTietRepository.findByGioHangId(gioHangEntity.getId());
            hoaDonEntity.setTongTien(tongtien);
            hoaDonEntity = hoaDonRepository.save(hoaDonEntity);

            // Lưu chi tiết hóa đơn
            for (GioHangChiTietEntity gioHang : gioHangChiTietEntities) {
                HoaDonChiTietEntity hoaDonChiTietEntity = new HoaDonChiTietEntity();
                hoaDonChiTietEntity.setSanPhamChiTiet(gioHang.getSanPhamChiTiet());
                hoaDonChiTietEntity.setHoaDon(hoaDonEntity);
                hoaDonChiTietEntity.setSoLuong(gioHang.getSoLuong());
                BigDecimal thanhTien = gioHang.getSanPhamChiTiet().getGiaSanPham().multiply(BigDecimal.valueOf(gioHang.getSoLuong()));
                hoaDonChiTietEntity.setThanhTien(thanhTien);
                hoaDonChiTietEntity.setUser(user.get());
                hoaDonChiTietRepository.save(hoaDonChiTietEntity);
                gioHangChiTietRepository.deleteByIdGH(gioHangEntity.getId());
                sanPhamChiTietRepository.updateSoLuong(gioHang.getSanPhamChiTiet().getId(), gioHang.getSoLuong());
            }

            return modelMapper.map(hoaDonEntity, HoaDonCHiTietCrud.class);

        } catch (IllegalArgumentException e) {
            System.err.println("Dữ liệu đầu vào không hợp lệ: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi trong quá trình xử lý hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public List<HoaDonChiTietDTO> getALlHoaDonCTByIdHoaDon(UUID idHoaDon) {
        Optional<HoaDonEntity> hoaDon =  hoaDonRepository.findById(idHoaDon);
        List<HoaDonChiTietEntity> entityList = hoaDonChiTietRepository.findByHoaDonChiTietByIdHoaDon(hoaDon.get().getId());
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, HoaDonChiTietDTO.class))
                .collect( Collectors.toList());
    }

    @Override
    public List<HoaDonDTO> getAllHoaDonByIdUser(UUID idUser) {
        Optional<UserEntity> user =  usersRepository.findById(idUser);
        List<HoaDonEntity> entityList = hoaDonRepository.findByHoaByIdUser(user.get().getId());
        return entityList.stream()
                .map(entity -> modelMapper.map(entity, HoaDonDTO.class))
                .collect( Collectors.toList());
    }

}
