package com.example.datn.service.impl;

import com.example.datn.Repository.KichCoRepository;
import com.example.datn.Repository.MauSacRepository;
import com.example.datn.dto.KichCoDTO;
import com.example.datn.dto.MauSacDTO;
import com.example.datn.entity.KichCoEntity;
import com.example.datn.entity.MauSacEntity;
import com.example.datn.service.KichCoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KichCoIMPL implements KichCoService {
    private final KichCoRepository kichCoRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<KichCoDTO> getAllKichCo() {
        List<KichCoEntity>kichCoEntities = kichCoRepository.findAll();
        return kichCoEntities.stream()
                .map(entity -> modelMapper.map(entity, KichCoDTO.class))
                .collect(Collectors.toList());
    }
}
