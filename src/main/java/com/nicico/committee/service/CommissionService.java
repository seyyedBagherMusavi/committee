package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.Commission;
import com.nicico.committee.repository.CommissionRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionService extends CrudService<Commission> {

    public CommissionService(CommissionRepository repository) {
        super(repository);
    }
}
