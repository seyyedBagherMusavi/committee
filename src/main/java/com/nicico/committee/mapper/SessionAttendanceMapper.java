package com.nicico.committee.mapper;

import com.nicico.committee.dto.SessionAttendanceCreateDto;
import com.nicico.committee.dto.SessionAttendanceDto;
import com.nicico.committee.dto.SessionAttendanceUpdateDto;
import com.nicico.committee.entities.CommissionEntities.SessionAttendance;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class SessionAttendanceMapper implements DefaultMapper<SessionAttendanceDto, SessionAttendanceUpdateDto, SessionAttendanceCreateDto, SessionAttendance> {
    public SessionAttendanceDto toDto(SessionAttendance entity) {
        if (entity == null) return null;
        SessionAttendanceDto dto = new SessionAttendanceDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public SessionAttendance toEntity(SessionAttendanceCreateDto dto) {
        if (dto == null) return null;
        SessionAttendance entity = new SessionAttendance();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public SessionAttendance toEntity(SessionAttendanceUpdateDto dto) {
        if (dto == null) return null;
        SessionAttendance entity = new SessionAttendance();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
