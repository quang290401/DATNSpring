package com.example.datn.service.impl;

import com.example.datn.Repository.SanPhamChiTietRepository;
import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.dto.SanPhamChiTietFiterDTO;
import com.example.datn.entity.SanPhamChiTietEntity;

import com.example.datn.service.SanPhamChiTietService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class SanPhamChiTietIMPL implements SanPhamChiTietService {
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<SanPhamChiTietDTO> getAllSanPhamChiTiet(Integer totalPage, Integer totalItem, SanPhamChiTietFiterDTO form) {
        Pageable pageable = PageRequest.of(totalPage, totalItem);

        // Xây dựng Specification từ form (SanPhamChiTietFiterDTO)
        Specification<SanPhamChiTietEntity> specification = SpecificationProduct.buildWhere(form);

        // Kiểm tra nếu không có Specification hoặc Specification là null
        if (specification == null) {
            List<SanPhamChiTietDTO> emptyList = Collections.emptyList();
            return new PageImpl<>(emptyList, pageable, 0);
        }

        // Sử dụng Specification trong phương thức findByProductName
        Page<SanPhamChiTietEntity> entityPage = sanPhamChiTietRepository.findAll(specification, pageable);

        // Convert Page<SanPhamChiTietEntity> sang Page<SanPhamChiTietDTO>
        List<SanPhamChiTietDTO> dtos = entityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, SanPhamChiTietDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }


    @Override
    public SanPhamChiTietDTO findById(UUID id) {
        SanPhamChiTietEntity sanPhamChiTietEntity = sanPhamChiTietRepository.findById(id).orElseThrow();

        return modelMapper.map(sanPhamChiTietEntity, SanPhamChiTietDTO.class);
    }

    @Override
    public List<SanPhamChiTietDTO> GetForSP(Pageable pageable) {
        List<SanPhamChiTietEntity> entities = sanPhamChiTietRepository.findTop4SanPhamChiTiet(pageable);
        return entities.stream()
                .map(entity -> modelMapper.map(entity, SanPhamChiTietDTO.class))
                .collect(Collectors.toList());
    }
}
