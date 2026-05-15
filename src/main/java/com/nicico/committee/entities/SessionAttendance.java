package com.nicico.committee.entities.CommissionEntities;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Records attendance for a specific session and member assignment.
 */
@Entity
@Table(name = "session_attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionAttendance extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private String id;

    // ----- Session FK -----
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", insertable = false, updatable = false)
    private CommissionSession session;

    // ----- Assignment FK (the invited member) -----
    @Column(name = "assignment_id", nullable = false)
    private String assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", insertable = false, updatable = false)
    private CommissionMemberAssignment assignment;

    // ----- DelegateAssignment FK (if substitute) -----
    @Column(name = "delegate_assignment_id")
    private String delegateAssignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegate_assignment_id", insertable = false, updatable = false)
    private CommissionMemberAssignment delegateAssignment;

    // ----- AttendanceStatus -----
    @Column(name = "attendance_status", nullable = false)
    private String attendanceStatus;

    // ----- SignedMinutes flag -----
    @Column(name = "signed_minutes", nullable = false)
    private Boolean signedMinutes = false;

    // ----- ArrivalTime -----
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_time")
    private Date arrivalTime;

    // ----- DepartureTime -----
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    private Date departureTime;
}
