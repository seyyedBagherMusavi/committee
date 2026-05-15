package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.CommissionSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionSessionRepository extends JpaRepository<CommissionSession, String> {
}
