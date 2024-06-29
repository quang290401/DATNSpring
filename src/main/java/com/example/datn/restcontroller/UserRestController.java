package com.example.datn.restcontroller;

import com.example.datn.dto.UserDTO;
import com.example.datn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController  {
    private final UserService userService;
    @GetMapping("/{idUser}")
    public UserDTO getUserById (@PathVariable UUID idUser) {
        return userService.findById(idUser);
    }
}
