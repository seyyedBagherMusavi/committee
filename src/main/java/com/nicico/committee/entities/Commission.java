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

/**
 * Represents a commission (meeting) entity.
 */
@Entity
@Table(name = "commission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commission extends Auditable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private String id;

    // ----- CommissionType FK -----
    @Column(name = "commission_type_id", nullable = false)
    private String commissionTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_type_id", insertable = false, updatable = false)
    @Where(clause = "type = 'COMMISSION_TYPE'")
    private BaseInfo commissionType;

    // ----- Parent Commission FK -----
    @Column(name = "parent_commission_id")
    private String parentCommissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_commission_id", insertable = false, updatable = false)
    private Commission parentCommission;

    // ----- SystemOwner FK -----
    @Column(name = "system_owner_id", nullable = false)
    private String systemOwnerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_owner_id", insertable = false, updatable = false)
    @Where(clause = "type = 'SYSTEM_OWNER'")
    private BaseInfo systemOwner;

    // ----- IsNicicoChart flag -----
    @Column(name = "is_nicico_chart", nullable = false)
    private Boolean isNicicoChart = true;

    // ----- CommissionOwner (Organization) FK -----
    @Column(name = "commission_owner_id")
    private String commissionOwnerId;


    // ----- CommissionOwnerTitel -----
    @Column(name = "commission_owner_titel")
    private String commissionOwnerTitel;

    // ----- CommissionLocation FK (BaseInfo item) -----
    @Column(name = "commission_location_id", nullable = false)
    private String commissionLocationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_location_id", insertable = false, updatable = false)
    @Where(clause = "type = 'LOCATION'")
    private BaseInfo commissionLocation;

    // ----- CommissionAddress -----
    @Column(name = "commission_address", nullable = false)
    private String commissionAddress;

    // ----- CommissionTime -----
    @Column(name = "commission_time")
    private String commissionTime;

    // ----- Code (unique, system-generated) -----
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    // ----- Name (Persian) -----
    @Column(name = "name", nullable = false)
    private String name;

    // ----- Description -----
    @Column(name = "description")
    private String description;

    // ----- CommissionLevel FK (BaseInfo item) -----
    @Column(name = "commission_level_id", nullable = false)
    private String commissionLevelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_level_id", insertable = false, updatable = false)
    @Where(clause = "type = 'COMMISSION_LEVEL'")
    private BaseInfo commissionLevel;

    // ----- Document IDs (legal/regulatory) -----
    @Column(name = "document_ids")
    private String documentIds;

    // ----- MinMembersCommission (quorum) -----
    @Column(name = "min_members_commission", nullable = false)
    private Integer minMembersCommission;

    // ----- IsExtendDate flag -----
    @Column(name = "is_extend_date", nullable = false)
    private Boolean isExtendDate = true;

    // ----- IsActive flag -----
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
