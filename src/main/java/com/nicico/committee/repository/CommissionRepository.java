package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepository extends JpaRepository<Commission, String> {
}
