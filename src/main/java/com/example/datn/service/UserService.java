package com.example.datn.service;

import com.example.datn.dto.SanPhamChiTietFiterDTO;
import com.example.datn.dto.UserCrud;
import com.example.datn.dto.UserDTO;
import com.example.datn.dto.UsersFiterDTO;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
     UserDTO findByTaiKhoan(String taiKhoan);
     UserDTO findById(UUID id);
     UserCrud addUser(UserCrud userCrud);
    Page<UserDTO> findAll(Integer totalPage, Integer totalItem, UsersFiterDTO form);
}
