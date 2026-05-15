package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionPeriodicitySettings;
import com.nicico.committee.repository.CommissionPeriodicitySettingsRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionPeriodicitySettingsService {

    private final CommissionPeriodicitySettingsRepository repository;

    public CommissionPeriodicitySettingsService(CommissionPeriodicitySettingsRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CommissionPeriodicitySettings> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CommissionPeriodicitySettings findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public CommissionPeriodicitySettings create(CommissionPeriodicitySettings entity) {
        return repository.save(entity);
    }

    public CommissionPeriodicitySettings update(String id, CommissionPeriodicitySettings entity) {
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
