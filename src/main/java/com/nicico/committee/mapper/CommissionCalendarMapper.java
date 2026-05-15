package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionCalendarCreateDto;
import com.nicico.committee.dto.CommissionCalendarDto;
import com.nicico.committee.dto.CommissionCalendarUpdateDto;
import com.nicico.committee.entities.CommissionEntities.CommissionCalendar;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionCalendarMapper implements DefaultMapper<CommissionCalendarDto, CommissionCalendarUpdateDto, CommissionCalendarCreateDto, CommissionCalendar> {
    public CommissionCalendarDto toDto(CommissionCalendar entity) {
        if (entity == null) return null;
        CommissionCalendarDto dto = new CommissionCalendarDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public CommissionCalendar toEntity(CommissionCalendarCreateDto dto) {
        if (dto == null) return null;
        CommissionCalendar entity = new CommissionCalendar();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public CommissionCalendar toEntity(CommissionCalendarUpdateDto dto) {
        if (dto == null) return null;
        CommissionCalendar entity = new CommissionCalendar();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
