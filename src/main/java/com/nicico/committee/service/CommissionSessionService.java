package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionSession;
import com.nicico.committee.repository.CommissionSessionRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionSessionService {

    private final CommissionSessionRepository repository;

    public CommissionSessionService(CommissionSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CommissionSession> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CommissionSession findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public CommissionSession create(CommissionSession entity) {
        return repository.save(entity);
    }

    public CommissionSession update(String id, CommissionSession entity) {
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
