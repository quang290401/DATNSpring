package com.example.datn.service.impl;

import com.example.datn.Repository.HinhAnhRepository;
import com.example.datn.dto.HinhAnhDTO;
import com.example.datn.dto.MauSacDTO;
import com.example.datn.entity.HinhAnhEntity;
import com.example.datn.entity.MauSacEntity;
import com.example.datn.service.HinhAnhService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HinhAnhIMPL implements HinhAnhService {
private final HinhAnhRepository hinhAnhRepository;
private final ModelMapper modelMapper;
    @Override
    public List<HinhAnhDTO> getAllHinhAnh() {
        List<HinhAnhEntity>hinhAnhEntities = hinhAnhRepository.findAll();
        return hinhAnhEntities.stream()
                .map(entity -> modelMapper.map(entity, HinhAnhDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public HinhAnhDTO addHinhAnh(HinhAnhDTO hinhAnhDTO) {
        HinhAnhEntity hinhAnh = HinhAnhEntity.builder()
                .duongDan(hinhAnhDTO.getDuongDan())
                .build();
        hinhAnh.setTen("Adidas");
        hinhAnh.setCreateDate(LocalDate.now());
        hinhAnh.setUpdateDate(LocalDateTime.now());
        hinhAnh.setTrangThai(1);
        hinhAnhRepository.save(hinhAnh);
        return modelMapper.map(hinhAnh,HinhAnhDTO.class);
    }
}
