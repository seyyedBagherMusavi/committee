package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionSessionDto {
    private String id;
    private String commissionId;
    private Commission commission;
    private String code;
    private String sessionNumber;
    private Date sessionDate;
    private String sessionTime;
    private String locationId;
    private BaseInfo location;
    private String address;
    private String agenda;
    private String sessionNatureId;
    private BaseInfo sessionNature;
    private String statusId;
    private BaseInfo status;
    private String documentId;
}
