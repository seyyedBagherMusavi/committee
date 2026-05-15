package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionCalendarDto {
    private String id;
    private String commissionId;
    private Commission commission;
    private String periodicityId;
    private BaseInfo periodicity;
    private String meetingModelId;
    private BaseInfo meetingModel;
    private Date startDateCommission;
    private Date endDateCommission;
    private Date firstDateMeeting;
    private Date nextPlanDateMeeting;
    private Integer floatingDays;
    private String responsibleCommissionPositionId;
    private CommissionPosition responsibleCommissionPosition;
    private Boolean isActive = true;
    private CommissionPeriodicitySettings periodicitySettings;
}
