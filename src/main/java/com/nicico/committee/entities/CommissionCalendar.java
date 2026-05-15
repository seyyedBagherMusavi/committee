package com.nicico.companies.entities.CommissionEntities;

import com.nicico.companies.entities.BaseInfo;
import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents the meeting schedule (calendar) for a commission.
 */
@Entity
@Table(name = "commission_calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionCalendar extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private String id;

    // ----- Commission FK -----
    @Column(name = "commission_id", nullable = false)
    private String commissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", insertable = false, updatable = false)
    private Commission commission;

    // ----- Periodicity FK (BaseInfo) -----
    @Column(name = "periodicity_id", nullable = false)
    private String periodicityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodicity_id", insertable = false, updatable = false)
    @Where(clause = "type = 'PERIODICITY'")
    private BaseInfo periodicity;

    // ----- MeetingModel FK (BaseInfo) -----
    @Column(name = "meeting_model_id", nullable = false)
    private String meetingModelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_model_id", insertable = false, updatable = false)
    @Where(clause = "type = 'MEETING_MODEL'")
    private BaseInfo meetingModel;

    // ----- StartDateCommission -----
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date_commission", nullable = false)
    private Date startDateCommission;

    // ----- EndDateCommission -----
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date_commission")
    private Date endDateCommission;

    // ----- FirstDateMeeting -----
    @Temporal(TemporalType.DATE)
    @Column(name = "first_date_meeting")
    private Date firstDateMeeting;

    // ----- NextPlanDateMeeting -----
    @Temporal(TemporalType.DATE)
    @Column(name = "next_plan_date_meeting")
    private Date nextPlanDateMeeting;

    // ----- FloatingDays -----
    @Column(name = "floating_days")
    private Integer floatingDays;

    // ----- ResponsibleCommissionPosition FK -----
    @Column(name = "responsible_commission_position_id", nullable = false)
    private String responsibleCommissionPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_commission_position_id", insertable = false, updatable = false)
    private CommissionPosition responsibleCommissionPosition;

    // ----- IsActive flag -----
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // ----- PeriodicitySettings (one-to-one) -----
    @OneToOne(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CommissionPeriodicitySettings periodicitySettings;
}
