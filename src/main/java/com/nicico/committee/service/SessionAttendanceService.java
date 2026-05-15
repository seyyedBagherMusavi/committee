package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.SessionAttendance;
import com.nicico.committee.repository.SessionAttendanceRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionAttendanceService extends CrudService<SessionAttendance> {

    public SessionAttendanceService(SessionAttendanceRepository repository) {
        super(repository);
    }
}
