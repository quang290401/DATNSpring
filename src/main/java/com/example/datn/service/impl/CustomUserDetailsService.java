//package com.example.datn.service.impl;
//
//import com.example.datn.Repository.UsersRepository;
//import com.example.datn.entity.UserEntity;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.config.annotation.web.oauth2.resourceserver.JwtDsl;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Collections;
//
//
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UsersRepository usersRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        try {
//            UserEntity userEntity = usersRepository.findByTaiKhoan(username)
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//            return new User(
//                    userEntity.getTaiKhoan(),
//                    userEntity.getMatKhau(), // Mật khẩu đã được mã hóa
//                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userEntity.getVaiTro().getTenVaiTro()))
//            );
//        } catch (Exception reException) {
//            throw reException;
//        }
//    }
//
//}
