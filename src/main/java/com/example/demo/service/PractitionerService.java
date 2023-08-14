package com.example.demo.service;

import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.PractitionerEntity;
import com.example.demo.repository.PractitionerEntityRepository;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PractitionerService {

    @Autowired
    public PractitionerEntityRepository practitionerEntityRepository;

    public PractitionerEntity createPractitionerEntity(PractitionerEntity practitionerEntity) {
        return practitionerEntityRepository.save(practitionerEntity);
    }

    public PractitionerEntity createPractitionerEntity(Practitioner practitioner, OrganizationEntity organizationEntity) {
        PractitionerEntity practitionerEntity = new PractitionerEntity(practitioner, organizationEntity);
        return practitionerEntityRepository.save(practitionerEntity);
    }
}
