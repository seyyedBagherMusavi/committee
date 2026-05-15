package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionDto {
    private String id;
    private String commissionTypeId;
    private BaseInfo commissionType;
    private String parentCommissionId;
    private Commission parentCommission;
    private String systemOwnerId;
    private BaseInfo systemOwner;
    private Boolean isNicicoChart = true;
    private String commissionOwnerId;
    private String commissionOwnerTitel;
    private String commissionLocationId;
    private BaseInfo commissionLocation;
    private String commissionAddress;
    private String commissionTime;
    private String code;
    private String name;
    private String description;
    private String commissionLevelId;
    private BaseInfo commissionLevel;
    private String documentIds;
    private Integer minMembersCommission;
    private Boolean isExtendDate = true;
    private Boolean isActive = true;
}
