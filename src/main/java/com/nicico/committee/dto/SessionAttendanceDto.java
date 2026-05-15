package com.nicico.committee.dto;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.committee.entities.CommissionEntities.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionAttendanceDto {
    private String id;
    private String sessionId;
    private CommissionSession session;
    private String assignmentId;
    private CommissionMemberAssignment assignment;
    private String delegateAssignmentId;
    private CommissionMemberAssignment delegateAssignment;
    private String attendanceStatus;
    private Boolean signedMinutes = false;
    private Date arrivalTime;
    private Date departureTime;
}
