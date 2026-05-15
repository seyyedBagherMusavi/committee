package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.CommissionMemberAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionMemberAssignmentRepository extends JpaRepository<CommissionMemberAssignment, String> {
}
