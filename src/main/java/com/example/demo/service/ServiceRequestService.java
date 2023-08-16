package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.ServiceRequestEntityRepository;
import org.hl7.fhir.r4.model.*;
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
            PractitionerEntity commissionedpractitionerEntity,
            CoverageEntity coverage
    ) {
        ServiceRequestEntity serviceRequestEntity = new ServiceRequestEntity(
                patientEntity,
                Arrays.asList(conditionEntity),
                referralpractitionerEntity,
                commissionedpractitionerEntity,
                referralorganizationEntity,
                commissionedorganizationEntity,
                coverage
        );

        return serviceRequestEntityRepository.save(serviceRequestEntity);
    }

    public ServiceRequest readServiceRequestEntity(
            String patientName,
            String commissionedDoctorName,
            String referralDoctorName
    ) {
        ServiceRequestEntity serviceRequestEntity1 = serviceRequestEntityRepository.readServiceRequestEntity(
                patientName,
                commissionedDoctorName,
                referralDoctorName
        );

        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.PROPOSAL);
        serviceRequest.setSubject(new Reference("Patient/" + serviceRequestEntity1.getPatientEntity().getId()));
        serviceRequest.setRequester(new Reference("Practitioner/" + serviceRequestEntity1.getReferralDoctor().getId()));
        serviceRequest.setPerformer(Arrays.asList(new Reference("Practitioner/" + serviceRequestEntity1.getCommissionedDoctor().getId())));

        serviceRequestEntity1.getConditionEntityList()
                .stream()
                .forEach(
                        each -> {
                            serviceRequest.addReasonCode().setText(each.getPatientCondition());
                        }
                );

        serviceRequest.setOccurrence(new DateTimeType(serviceRequestEntity1.getOccurrence()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Requester Organization ID").setValue(serviceRequestEntity1.getCommissionedOrganization().getId().toString()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Performer Organization ID").setValue(serviceRequestEntity1.getReferralOrganization().getId().toString()));
        String coverageTypeValue;
        switch (serviceRequestEntity1.getCoverage().getCoverageTypeClass()) {
            case SELFPAY:
                coverageTypeValue = "SelfPay";
                break;
            case MEDICALAID:
                coverageTypeValue = "MedicalAid";
                break;
            case HEALTHINSURANCE:
                coverageTypeValue = "HealthInsurance";
                break;
            case INDUSTRIALACCIDENT:
                coverageTypeValue = "IndustrialAccident";
                break;
            default:
                coverageTypeValue = ""; // 또는 다른 기본값
                break;
        }

        serviceRequest.addExtension(new Extension().setUrl("coverage-kind").setValue(
                new Reference(new Coverage()
                        .setStatus(Coverage.CoverageStatus.ACTIVE)
                        .addExtension(new Extension().setUrl("coverage-kind").setValue(
                                new StringType(coverageTypeValue)
                        ))
                )
        ));

        return serviceRequest;
/*
        serviceRequest.addReasonCode().setText(conditionEntity.getPatientCondition() + serviceRequestEntity1.getConditionEntityList().);
        serviceRequest.setOccurrence(new DateTimeType(serviceRequestEntity.getOccurrence()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Requester Organization ID").setValue(organizationEntity1.getId().toString()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Performer Organization ID").setValue(organizationEntity2.getId().toString()));
        serviceRequest.addExtension(new Extension().setUrl("coverage-Type").setValue(new Reference(parsedCoverage)));

        return iParser.encodeResourceToString(serviceRequest);*/
    }
}
