package com.nicico.committee.mapper;

import com.nicico.committee.dto.CommissionMemberAssignmentCreateDto;
import com.nicico.committee.dto.CommissionMemberAssignmentDto;
import com.nicico.committee.dto.CommissionMemberAssignmentUpdateDto;
import com.nicico.committee.entities.CommissionEntities.CommissionMemberAssignment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CommissionMemberAssignmentMapper implements DefaultMapper<CommissionMemberAssignmentDto, CommissionMemberAssignmentUpdateDto, CommissionMemberAssignmentCreateDto, CommissionMemberAssignment> {
    public CommissionMemberAssignmentDto toDto(CommissionMemberAssignment entity) {
        if (entity == null) return null;
        CommissionMemberAssignmentDto dto = new CommissionMemberAssignmentDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    public CommissionMemberAssignment toEntity(CommissionMemberAssignmentCreateDto dto) {
        if (dto == null) return null;
        CommissionMemberAssignment entity = new CommissionMemberAssignment();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
    public CommissionMemberAssignment toEntity(CommissionMemberAssignmentUpdateDto dto) {
        if (dto == null) return null;
        CommissionMemberAssignment entity = new CommissionMemberAssignment();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
