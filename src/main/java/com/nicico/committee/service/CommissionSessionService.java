package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionSession;
import com.nicico.committee.repository.CommissionSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionSessionService extends CrudService<CommissionSession> {

    public CommissionSessionService(CommissionSessionRepository repository) {
        super(repository);
    }
}
