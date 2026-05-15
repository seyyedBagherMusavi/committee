package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionSessionCreateDto;
import com.nicico.committee.dto.CommissionSessionDto;
import com.nicico.committee.dto.CommissionSessionUpdateDto;
import com.nicico.committee.entities.CommissionEntities.CommissionSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionSessionMapper implements DefaultMapper<CommissionSessionDto, CommissionSessionUpdateDto, CommissionSessionCreateDto, CommissionSession> {
    public CommissionSessionDto toDto(CommissionSession entity) {
        if (entity == null) return null;
        CommissionSessionDto dto = new CommissionSessionDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public CommissionSession toEntity(CommissionSessionCreateDto dto) {
        if (dto == null) return null;
        CommissionSession entity = new CommissionSession();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public CommissionSession toEntity(CommissionSessionUpdateDto dto) {
        if (dto == null) return null;
        CommissionSession entity = new CommissionSession();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
