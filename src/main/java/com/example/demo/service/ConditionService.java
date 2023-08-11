package com.example.demo.service;

import com.example.demo.entity.ConditionEntity;
import com.example.demo.entity.PatientEntity;
import com.example.demo.repository.ConditionEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {

    @Autowired
    private ConditionEntityRepository conditionEntityRepository;

    public ConditionEntity createConditionEntity(ConditionEntity condition) {
        return conditionEntityRepository.save(condition);
    }

    public List<ConditionEntity> searchByConditionByPatientEntity(PatientEntity patient) {
        return conditionEntityRepository.findConditionEntitiesByPatient(patient);
    }
}
