package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionPosition;
import com.nicico.committee.repository.CommissionPositionRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionPositionService {

    private final CommissionPositionRepository repository;

    public CommissionPositionService(CommissionPositionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CommissionPosition> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CommissionPosition findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public CommissionPosition create(CommissionPosition entity) {
        return repository.save(entity);
    }

    public CommissionPosition update(String id, CommissionPosition entity) {
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
