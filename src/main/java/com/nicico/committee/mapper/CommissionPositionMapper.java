package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionPositionCreateDto;
import com.nicico.committee.dto.CommissionPositionDto;
import com.nicico.committee.dto.CommissionPositionUpdateDto;
import com.nicico.committee.entities.CommissionEntities.CommissionPosition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionPositionMapper implements DefaultMapper<CommissionPositionDto, CommissionPositionUpdateDto, CommissionPositionCreateDto, CommissionPosition> {
    public CommissionPositionDto toDto(CommissionPosition entity) {
        if (entity == null) return null;
        CommissionPositionDto dto = new CommissionPositionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public CommissionPosition toEntity(CommissionPositionCreateDto dto) {
        if (dto == null) return null;
        CommissionPosition entity = new CommissionPosition();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public CommissionPosition toEntity(CommissionPositionUpdateDto dto) {
        if (dto == null) return null;
        CommissionPosition entity = new CommissionPosition();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
