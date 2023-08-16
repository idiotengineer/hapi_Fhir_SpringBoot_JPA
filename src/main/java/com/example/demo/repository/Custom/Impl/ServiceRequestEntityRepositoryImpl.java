package com.example.demo.repository.Custom.Impl;

import com.example.demo.entity.ServiceRequestEntity;
import com.example.demo.repository.Custom.ServiceRequestEntityRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hl7.fhir.r4.model.codesystems.ConditionClinical;
import org.hl7.fhir.r4.model.codesystems.RequestStatus;

import static com.example.demo.entity.QConditionEntity.conditionEntity;
import static com.example.demo.entity.QCoverageEntity.coverageEntity;
import static com.example.demo.entity.QOrganizationEntity.organizationEntity;
import static com.example.demo.entity.QPatientEntity.patientEntity;
import static com.example.demo.entity.QPractitionerEntity.practitionerEntity;
import static com.example.demo.entity.QServiceRequestEntity.serviceRequestEntity;

@RequiredArgsConstructor
public class ServiceRequestEntityRepositoryImpl implements ServiceRequestEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ServiceRequestEntity readServiceRequestEntity(
            String patientName,
            String commissionedDoctorName,
            String referralDoctorName
    ) {

        return jpaQueryFactory
                .selectFrom(serviceRequestEntity)
                .join(serviceRequestEntity.patientEntity, patientEntity).fetchJoin()
                .join(serviceRequestEntity.commissionedDoctor, practitionerEntity).fetchJoin()
                .join(serviceRequestEntity.referralDoctor,practitionerEntity).fetchJoin()
                .join(serviceRequestEntity.commissionedOrganization, organizationEntity).fetchJoin()
                .join(serviceRequestEntity.referralOrganization,organizationEntity).fetchJoin()
                .join(serviceRequestEntity.conditionEntityList, conditionEntity).fetchJoin()
                .join(serviceRequestEntity.coverage, coverageEntity).fetchJoin()
                .where(
                        serviceRequestEntity.patientEntity.name.eq(patientName)
                                .and(conditionEntity.conditionClinical.eq(ConditionClinical.ACTIVE))
                                .and(serviceRequestEntity.commissionedDoctor.name.eq(commissionedDoctorName))
                                .and(serviceRequestEntity.referralDoctor.name.eq(referralDoctorName))
                ).fetchOne();
    }
}
