package com.example.datn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SuperDTO {
    private UUID id;
    private LocalDate createDate;
    private LocalDate modifyDate;
}
