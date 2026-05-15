package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionPosition;
import com.nicico.committee.repository.CommissionPositionRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionPositionService extends CrudService<CommissionPosition> {

    public CommissionPositionService(CommissionPositionRepository repository) {
        super(repository);
    }
}
