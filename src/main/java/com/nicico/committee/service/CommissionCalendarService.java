package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionCalendar;
import com.nicico.committee.repository.CommissionCalendarRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionCalendarService extends CrudService<CommissionCalendar> {

    public CommissionCalendarService(CommissionCalendarRepository repository) {
        super(repository);
    }
}
