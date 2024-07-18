package com.example.datn.service.impl;

import com.example.datn.Repository.SanPhamChiTietRepository;
import com.example.datn.Repository.SanPhamRepository;
import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.dto.SanPhamDTO;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.entity.SanPhamEntity;
import com.example.datn.service.SanPhamService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class SanPhamIMPL implements SanPhamService {
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<SanPhamDTO> getAllSanPham(Integer totalPage, Integer totalItem) {
        Pageable pageable = PageRequest.of(totalPage, totalItem);


        Page<SanPhamEntity> entityPage = sanPhamRepository.findAll(pageable);

        // Convert Page<SanPhamChiTietEntity> sang Page<SanPhamChiTietDTO>
        List<SanPhamDTO> dtos = entityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, SanPhamDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }
}
