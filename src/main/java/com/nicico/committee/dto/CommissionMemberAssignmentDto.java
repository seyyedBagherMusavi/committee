package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionMemberAssignmentDto {
    private String id;
    private String commissionPositionId;
    private CommissionPosition commissionPosition;
    private String nicicoPositionId;
    private String nationalCode;
    private Boolean alternateMember = false;
    private String altNicicoPositionId;
    private String altMemberNationalCode;
    private String nonicicoTitel;
    private Date startDate;
    private Date endDate;
    private Boolean isExtendDate = true;
    private Boolean isActive = true;
    private String documentIds;
}
