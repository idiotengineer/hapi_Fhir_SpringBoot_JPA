package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class OrganizationEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String hospitalCode;

    @OneToMany(mappedBy = "organizationEntity") // 의사 명단
    private List<PractitionerEntity> practitionerEntityList;

    @OneToMany(mappedBy = "referralOrganization") // 의뢰받은 진료의뢰서
    private List<ServiceRequestEntity> commissionedServiceRequestEntity;

    @OneToMany(mappedBy = "commissionedOrganization") //의뢰한 진료의뢰서
    private List<ServiceRequestEntity> referralServiceRequestEntity;
}
