package com.example.demo.entity;

import lombok.*;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
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

    public OrganizationEntity(Organization organization) {
        this.setName(organization.getName());
        this.setHospitalCode(hospitalCode);
        this.setPractitionerEntityList(new ArrayList<>());
        this.setReferralServiceRequestEntity(new ArrayList<>());
        this.setCommissionedServiceRequestEntity(new ArrayList<>());
    }


    public Organization translateToOrganizationResource() {
        Organization organization = new Organization();

        organization.setName(this.name);
        organization.addIdentifier(new Identifier().setSystem("HospitalCode").setValue(this.getHospitalCode()));
        organization.addIdentifier(new Identifier().setSystem("ID").setValue(this.getId().toString()));

        return organization;
    }
}
