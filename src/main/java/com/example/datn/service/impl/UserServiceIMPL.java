package com.example.datn.service.impl;

import com.example.datn.Repository.DiaChiRepository;
import com.example.datn.Repository.GioHangRepository;
import com.example.datn.Repository.UsersRepository;
import com.example.datn.Repository.VaiTroRepository;
import com.example.datn.dto.UserCrud;
import com.example.datn.dto.UserDTO;
import com.example.datn.entity.*;

import com.example.datn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {
    private UsersRepository usersRepository;
    private final VaiTroRepository vaiTroRepository;
    private final GioHangRepository gioHangRepository;
    private final DiaChiRepository diaChiRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public void CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @Override
    public UserDTO findByTaiKhoan(String taiKhoan) {
        Optional<UserEntity> user = usersRepository.findByTaiKhoan(taiKhoan);
        if (user.isPresent()) {
            return modelMapper.map(user.get(), UserDTO.class);
        } else {
            return null;  // hoặc ném ngoại lệ tùy thuộc vào logic của bạn
        }
    }

    @Override
    public UserDTO findById(UUID id) {
        Optional<UserEntity> user = usersRepository.findById(id);
        return user.map(value -> modelMapper.map(value, UserDTO.class)).orElse(null);
    }

    @Override
    public UserCrud addUser(UserCrud userCrud) {
        UUID idDiaChi =UUID.randomUUID();
        UUID idGioHang =UUID.randomUUID();
        DiaChiEntity diaChiEntity = new DiaChiEntity();
        diaChiEntity.setId(idDiaChi);
        diaChiEntity.setTinh("0");
        diaChiEntity.setHuyen("0");
        diaChiEntity.setXa("0");
        diaChiEntity.setDiaChi("0");
        diaChiEntity.setTrangThai(1);
        diaChiEntity.setCreateDate(LocalDate.now());
        diaChiEntity.setUpdateDate(LocalDateTime.now());
        diaChiRepository.save(diaChiEntity);
        VaiTroEntity vaiTroEntity = vaiTroRepository.findByTenVaiTroUser();
//        PasswordEncoder passwordEncoder  = new BCryptPasswordEncoder(10);
        UserEntity userEntity = UserEntity.builder()
                .diaChi(diaChiEntity)
                .vaiTro(vaiTroEntity)
                .taiKhoan(userCrud.getTaiKhoan())
                .matKhau(userCrud.getMatKhau())
                .ho(userCrud.getHo())
                .tenDem(userCrud.getTenDem())
                .ten(userCrud.getTen())
                .sdt(userCrud.getSdt())
                .ngaySinh(userCrud.getNgaySinh())
                .build();
        usersRepository.save(userEntity);
        GioHangEntity gioHangEntity = new GioHangEntity();
        gioHangEntity.setId(idGioHang);
        gioHangEntity.setCreateDate(LocalDate.now());
        gioHangEntity.setUpdateDate(LocalDateTime.now());
        gioHangEntity.setUser(userEntity);
        gioHangRepository.save(gioHangEntity);
        return  modelMapper.map(userEntity, UserCrud.class);
    }
}
