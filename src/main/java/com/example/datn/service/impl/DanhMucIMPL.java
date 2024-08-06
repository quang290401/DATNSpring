package com.example.datn.service.impl;

import com.example.datn.Repository.DanhMucRepository;
import com.example.datn.dto.DanhMucDTO;
import com.example.datn.entity.DanhMucEntity;
import com.example.datn.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DanhMucIMPL implements DanhMucService {
    private final DanhMucRepository danhMucRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DanhMucDTO> getAllDanhMuc() {
        List<DanhMucEntity>danhMucEntities = danhMucRepository.findAll();
        return danhMucEntities.stream()
                .map(entity -> modelMapper.map(entity, DanhMucDTO.class))
                .collect(Collectors.toList());
    }
}
