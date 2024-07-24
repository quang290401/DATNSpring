package com.example.datn.service.impl;

import com.example.datn.Repository.ChatLieuRepository;
import com.example.datn.Repository.KichCoRepository;
import com.example.datn.dto.ChatLieuDTO;
import com.example.datn.dto.KichCoDTO;
import com.example.datn.entity.ChatLieuEntity;
import com.example.datn.entity.KichCoEntity;
import com.example.datn.service.ChatLieuService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatLieuIMPL implements ChatLieuService {
    private final ChatLieuRepository chatLieuRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ChatLieuDTO> getAllChatLieu() {
        List<ChatLieuEntity>chatLieuEntities = chatLieuRepository.findAll();
        return chatLieuEntities.stream()
                .map(entity -> modelMapper.map(entity, ChatLieuDTO.class))
                .collect(Collectors.toList());
    }
}
