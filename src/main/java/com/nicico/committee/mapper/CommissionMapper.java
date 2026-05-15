package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionCreateDto;
import com.nicico.committee.dto.CommissionDto;
import com.nicico.committee.dto.CommissionUpdateDto;
import com.nicico.committee.entities.CommissionEntities.Commission;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionMapper implements DefaultMapper<CommissionDto, CommissionUpdateDto, CommissionCreateDto, Commission> {
    public CommissionDto toDto(Commission entity) {
        if (entity == null) return null;
        CommissionDto dto = new CommissionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public Commission toEntity(CommissionCreateDto dto) {
        if (dto == null) return null;
        Commission entity = new Commission();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public Commission toEntity(CommissionUpdateDto dto) {
        if (dto == null) return null;
        Commission entity = new Commission();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
