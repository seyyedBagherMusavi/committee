package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionPeriodicitySettingsCreateDto;
import com.nicico.committee.dto.CommissionPeriodicitySettingsDto;
import com.nicico.committee.dto.CommissionPeriodicitySettingsUpdateDto;
import com.nicico.committee.entities.CommissionEntities.CommissionPeriodicitySettings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionPeriodicitySettingsMapper implements DefaultMapper<CommissionPeriodicitySettingsDto, CommissionPeriodicitySettingsUpdateDto, CommissionPeriodicitySettingsCreateDto, CommissionPeriodicitySettings> {
    public CommissionPeriodicitySettingsDto toDto(CommissionPeriodicitySettings entity) {
        if (entity == null) return null;
        CommissionPeriodicitySettingsDto dto = new CommissionPeriodicitySettingsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public CommissionPeriodicitySettings toEntity(CommissionPeriodicitySettingsCreateDto dto) {
        if (dto == null) return null;
        CommissionPeriodicitySettings entity = new CommissionPeriodicitySettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public CommissionPeriodicitySettings toEntity(CommissionPeriodicitySettingsUpdateDto dto) {
        if (dto == null) return null;
        CommissionPeriodicitySettings entity = new CommissionPeriodicitySettings();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
