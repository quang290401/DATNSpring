package com.example.datn.service;

import com.example.datn.dto.UserDTO;

import java.util.UUID;

public interface UserService {
     UserDTO findByTaiKhoan(String taiKhoan);
     UserDTO findById(UUID id);
}
