package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionPositionDto {
    private String id;
    private String commissionId;
    private Commission commission;
    private String positionId;
    private BaseInfo position;
    private Boolean isNicicoChart = true;
    private Integer numMember = 1;
    private Boolean canVote = false;
    private Boolean canSignature = false;
    private Boolean canAlternate = false;
    private String canSeeDoc;
    private String presenceTypeId;
    private BaseInfo presenceType;
    private String attendanceConditionId;
    private BaseInfo attendanceCondition;
    private Boolean isActive;
}
