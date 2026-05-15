package com.nicico.committee.service;

import com.nicico.committee.entities.CommissionEntities.CommissionPeriodicitySettings;
import com.nicico.committee.repository.CommissionPeriodicitySettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class CommissionPeriodicitySettingsService extends CrudService<CommissionPeriodicitySettings> {

    public CommissionPeriodicitySettingsService(CommissionPeriodicitySettingsRepository repository) {
        super(repository);
    }
}
