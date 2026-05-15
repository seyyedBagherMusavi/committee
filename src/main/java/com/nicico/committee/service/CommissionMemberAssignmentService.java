package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionMemberAssignment;
import com.nicico.committee.repository.CommissionMemberAssignmentRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionMemberAssignmentService {

    private final CommissionMemberAssignmentRepository repository;

    public CommissionMemberAssignmentService(CommissionMemberAssignmentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CommissionMemberAssignment> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CommissionMemberAssignment findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public CommissionMemberAssignment create(CommissionMemberAssignment entity) {
        return repository.save(entity);
    }

    public CommissionMemberAssignment update(String id, CommissionMemberAssignment entity) {
        if (!repository.existsById(id)) throw new EntityNotFoundException("Entity not found: " + id);
        BeanWrapper bw = new BeanWrapperImpl(entity);
        bw.setPropertyValue("id", id);
        return repository.save(entity);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) throw new EntityNotFoundException("Entity not found: " + id);
        repository.deleteById(id);
    }
}
