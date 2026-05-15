package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.CommissionPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionPositionRepository extends JpaRepository<CommissionPosition, String> {
}
