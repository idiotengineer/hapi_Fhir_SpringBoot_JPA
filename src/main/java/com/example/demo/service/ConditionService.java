package com.example.demo.service;

import com.example.demo.entity.ConditionEntity;
import com.example.demo.entity.PatientEntity;
import com.example.demo.repository.ConditionEntityRepository;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.codesystems.ConditionClinical;
import org.hl7.fhir.r4.model.codesystems.ConditionVerStatus;
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

    public ConditionEntity createConditionEntityByPatientEntity(Condition condition,PatientEntity patient) {
        ConditionEntity conditionEntity = new ConditionEntity(condition, patient);
        return conditionEntityRepository.save(conditionEntity);
    }
}
