package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.codesystems.RequestIntent;
import org.hl7.fhir.r4.model.codesystems.RequestPriority;
import org.hl7.fhir.r4.model.codesystems.RequestStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServiceRequestEntity {

    @Id
    @GeneratedValue
    @Column(name = "service_entity_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Enumerated(EnumType.STRING)
    private RequestIntent requestIntent;

    @Enumerated(EnumType.STRING)
    private RequestPriority requestPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PatientEntity patientEntity;

    @OneToMany(mappedBy = "serviceRequestEntity")
    private List<ConditionEntity> conditionEntityList = new ArrayList<>();

    // 의뢰하는 의사 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PractitionerEntity referralDoctor;

    // 의뢰받는 의사 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PractitionerEntity commissionedDoctor;

    // 의뢰하는 병원 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrganizationEntity referralOrganization;

    // 의뢰받는 병원 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrganizationEntity commissionedOrganization;

    private Date occurrence;
}