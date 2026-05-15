package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.CommissionCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionCalendarRepository extends JpaRepository<CommissionCalendar, String> {
}
