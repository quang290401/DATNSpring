package com.example.datn.service.impl;

import com.example.datn.Repository.DanhMucRepository;
import com.example.datn.Repository.MauSacRepository;
import com.example.datn.dto.DanhMucDTO;
import com.example.datn.dto.MauSacDTO;
import com.example.datn.entity.DanhMucEntity;
import com.example.datn.entity.MauSacEntity;
import com.example.datn.service.MauSacService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MauSacIMPL implements MauSacService {

    private final MauSacRepository mauSacRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<MauSacDTO> getAllMauSac() {
        List<MauSacEntity>mauSacEntities = mauSacRepository.findAll();
        return mauSacEntities.stream()
                .map(entity -> modelMapper.map(entity, MauSacDTO.class))
                .collect(Collectors.toList());
    }
}
