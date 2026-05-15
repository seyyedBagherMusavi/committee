package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.SessionAttendance;
import com.nicico.committee.repository.SessionAttendanceRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class SessionAttendanceService {

    private final SessionAttendanceRepository repository;

    public SessionAttendanceService(SessionAttendanceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<SessionAttendance> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public SessionAttendance findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found: " + id));
    }

    public SessionAttendance create(SessionAttendance entity) {
        return repository.save(entity);
    }

    public SessionAttendance update(String id, SessionAttendance entity) {
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
