package com.example.datn.service.impl;

import com.example.datn.Repository.UsersRepository;
import com.example.datn.dto.UserDTO;
import com.example.datn.entity.UserEntity;

import com.example.datn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceIMPL implements UserService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
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
}
