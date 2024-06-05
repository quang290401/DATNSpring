package com.example.datn.service.impl;

import com.example.datn.dto.SanPhamChiTietDTO;
import com.example.datn.entity.SanPhamChiTietEntity;
import com.example.datn.repository.SanPhamChiTietRepository;
import com.example.datn.service.SanPhamChiTietService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class SanPhamChiTietIMPL implements SanPhamChiTietService {
 private final SanPhamChiTietRepository sanPhamChiTietRepository;
 private final ModelMapper modelMapper;

    @Override
    public Page<SanPhamChiTietDTO> getAllSanPhamChiTiet(Integer totalPage, Integer totalItem) {
        Pageable  pageable = PageRequest.of(totalPage,totalItem);
        Page<SanPhamChiTietEntity> entityPage = sanPhamChiTietRepository.findAll(pageable);
        List<SanPhamChiTietDTO> dtos = modelMapper.map(entityPage.getContent(), new TypeToken<List<SanPhamChiTietDTO>>() {
        }.getType());
        Page<SanPhamChiTietDTO> dtoPage = new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
        return dtoPage;
    }
}
