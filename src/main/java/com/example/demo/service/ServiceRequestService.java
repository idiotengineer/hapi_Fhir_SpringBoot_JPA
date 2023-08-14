package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.ServiceRequestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ServiceRequestService {

    @Autowired
    ServiceRequestEntityRepository serviceRequestEntityRepository;

    public ServiceRequestEntity createServiceRequestEntity(
            PatientEntity patientEntity,
            ConditionEntity conditionEntity,
            OrganizationEntity referralorganizationEntity,
            PractitionerEntity referralpractitionerEntity,
            OrganizationEntity commissionedorganizationEntity,
            PractitionerEntity commissionedpractitionerEntity
    ) {
        ServiceRequestEntity serviceRequestEntity = new ServiceRequestEntity(
                patientEntity,
                Arrays.asList(conditionEntity),
                referralpractitionerEntity,
                commissionedpractitionerEntity,
                referralorganizationEntity,
                commissionedorganizationEntity
        );

        return serviceRequestEntityRepository.save(serviceRequestEntity);
    }
}
