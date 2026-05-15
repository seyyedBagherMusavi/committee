package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.Commission;
import com.nicico.committee.repository.CommissionRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionService {

    private final CommissionRepository repository;

    public CommissionService(CommissionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Commission> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Commission findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public Commission create(Commission entity) {
        return repository.save(entity);
    }

    public Commission update(String id, Commission entity) {
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
