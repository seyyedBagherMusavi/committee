package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.CommissionPeriodicitySettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionPeriodicitySettingsRepository extends JpaRepository<CommissionPeriodicitySettings, String> {
}
