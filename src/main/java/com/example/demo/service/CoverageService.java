package com.example.demo.service;

import com.example.demo.entity.CoverageEntity;
import com.example.demo.entity.PatientEntity;
import com.example.demo.repository.CoverageEntityRepository;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoverageService {

    @Autowired
    CoverageEntityRepository coverageEntityRepository;

    public CoverageEntity CreateCoverageEntity(Coverage coverage, PatientEntity patient) {
        CoverageEntity coverageEntity = new CoverageEntity(coverage, patient);
        return coverageEntityRepository.save(coverageEntity);
    }
}
