package com.nicico.companies.entities.CommissionEntities;

import com.nicico.copper.common.domain.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents the assignment of a member (or alternate) to a commission position.
 */
@Entity
@Table(name = "commission_member_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionMemberAssignment extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private String id;

    // ----- CommissionPosition FK -----
    @Column(name = "commission_position_id", nullable = false)
    private String commissionPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_position_id", insertable = false, updatable = false)
    private CommissionPosition commissionPosition;

    // ----- NicicoPosition (OrganizationalPosition) FK -----
    @Column(name = "nicico_position_id")
    private String nicicoPositionId;


    // ----- NationalCode -----
    @Column(name = "national_code", nullable = false)
    private String nationalCode;

    // ----- AlternateMember flag -----
    @Column(name = "alternate_member", nullable = false)
    private Boolean alternateMember = false;

    // ----- AltNicicoPosition FK -----
    @Column(name = "alt_nicico_position_id")
    private String altNicicoPositionId;


    // ----- AltMemberNationalCode -----
    @Column(name = "alt_member_national_code")
    private String altMemberNationalCode;

    // ----- NonicicoTitel (for non-company persons) -----
    @Column(name = "nonicico_titel")
    private String nonicicoTitel;

    // ----- StartDate -----
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    // ----- EndDate -----
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    // ----- IsExtendDate flag -----
    @Column(name = "is_extend_date", nullable = false)
    private Boolean isExtendDate = true;

    // ----- IsActive flag -----
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // ----- DocumentIDs (appointment documents) -----
    @Column(name = "document_ids")
    private String documentIds;
}
