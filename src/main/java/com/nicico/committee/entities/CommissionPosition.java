package com.nicico.committee.entities.CommissionEntities;

import com.nicico.committee.entities.BaseInfo;
import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Defines a position (role) within a commission.
 */
@Entity
@Table(name = "commission_position")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionPosition extends Auditable {

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

    // ----- Position type FK (BaseInfo) -----
    @Column(name = "position_id", nullable = false)
    private String positionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    @Where(clause = "type = 'COMMISSION_POSITION'")
    private BaseInfo position;

    // ----- IsNicicoChart flag -----
    @Column(name = "is_nicico_chart", nullable = false)
    private Boolean isNicicoChart = true;

    // ----- NumMember -----
    @Column(name = "num_member", nullable = false)
    private Integer numMember = 1;

    // ----- CanVote flag -----
    @Column(name = "can_vote", nullable = false)
    private Boolean canVote = false;

    // ----- CanSignature flag -----
    @Column(name = "can_signature", nullable = false)
    private Boolean canSignature = false;

    // ----- CanAlternate flag -----
    @Column(name = "can_alternate", nullable = false)
    private Boolean canAlternate = false;

    // ----- CanSeeDoc (document permissions) -----
    @Column(name = "can_see_doc", nullable = false)
    private String canSeeDoc;

    // ----- PresenceType FK (BaseInfo) -----
    @Column(name = "presence_type_id", nullable = false)
    private String presenceTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presence_type_id", insertable = false, updatable = false)
    @Where(clause = "type = 'PRESENCE_TYPE'")
    private BaseInfo presenceType;

    // ----- AttendanceCondition FK (BaseInfo) -----
    @Column(name = "attendance_condition_id", nullable = false)
    private String attendanceConditionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_condition_id", insertable = false, updatable = false)
    @Where(clause = "type = 'ATTENDANCE_CONDITION'")
    private BaseInfo attendanceCondition;

    // ----- IsActive flag -----
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
