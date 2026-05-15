package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommissionPeriodicitySettingsDto {
    private String id;
    private String calendarId;
    private CommissionCalendar calendar;
    private Integer intervalValue;
    private String weekdayMask;
    private String dayOfMonth;
    private String weekOfMonth;
    private String monthMask;
    private String holidayRule;
}
