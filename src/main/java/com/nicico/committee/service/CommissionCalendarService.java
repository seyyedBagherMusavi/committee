package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionCalendar;
import com.nicico.committee.repository.CommissionCalendarRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommissionCalendarService {

    private final CommissionCalendarRepository repository;

    public CommissionCalendarService(CommissionCalendarRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CommissionCalendar> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public CommissionCalendar findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public CommissionCalendar create(CommissionCalendar entity) {
        return repository.save(entity);
    }

    public CommissionCalendar update(String id, CommissionCalendar entity) {
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
