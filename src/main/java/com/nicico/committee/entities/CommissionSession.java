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
import java.util.Date;

/**
 * A specific session (meeting instance) of a commission.
 */
@Entity
@Table(name = "commission_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionSession extends Auditable {

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

    // ----- Code (immutable from Commission) -----
    @Column(name = "code", nullable = false)
    private String code;

    // ----- SessionNumber (system-generated, e.g., 1405-001) -----
    @Column(name = "session_number", nullable = false)
    private String sessionNumber;

    // ----- SessionDate -----
    @Temporal(TemporalType.DATE)
    @Column(name = "session_date", nullable = false)
    private Date sessionDate;

    // ----- SessionTime -----
    @Column(name = "session_time", nullable = false)
    private String sessionTime;

    // ----- Location FK (BaseInfo item) -----
    @Column(name = "location_id")
    private String locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    @Where(clause = "type = 'LOCATION'")
    private BaseInfo location;

    // ----- Address -----
    @Column(name = "address")
    private String address;

    // ----- Agenda -----
    @Column(name = "agenda", nullable = false)
    private String agenda;

    // ----- SessionNature FK (BaseInfo) -----
    @Column(name = "session_nature_id", nullable = false)
    private String sessionNatureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_nature_id", insertable = false, updatable = false)
    @Where(clause = "type = 'SESSION_NATURE'")
    private BaseInfo sessionNature;

    // ----- Status FK (BaseInfo) -----
    @Column(name = "status_id", nullable = false)
    private String statusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    @Where(clause = "type = 'SESSION_STATUS'")
    private BaseInfo status;

    // ----- Document (final signed minutes) FK -----
    @Column(name = "document_id")
    private String documentId;

}
