package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionMemberAssignment;
import com.nicico.committee.repository.CommissionMemberAssignmentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionMemberAssignmentService extends CrudService<CommissionMemberAssignment> {

    public CommissionMemberAssignmentService(CommissionMemberAssignmentRepository repository) {
        super(repository);
    }
}
