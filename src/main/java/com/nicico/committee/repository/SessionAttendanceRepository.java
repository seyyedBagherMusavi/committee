package com.nicico.committee.repository;

import com.nicico.committee.entities.CommissionEntities.SessionAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, String> {
}
