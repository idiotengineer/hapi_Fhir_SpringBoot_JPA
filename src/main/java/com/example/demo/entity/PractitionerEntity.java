package com.example.demo.entity;

import lombok.*;
import org.hl7.fhir.r4.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PractitionerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String licenseNumber;

    @ElementCollection
    @CollectionTable(name = "practitioner_phone_number_table")
    @Column(name = "phoneNumber")
    private List<String> contact = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrganizationEntity organizationEntity;

    @OneToMany(mappedBy = "referralDoctor") // 의뢰한 진료요청서
    private List<ServiceRequestEntity> commissionedServiceRequestEntity = new ArrayList<>();

    @OneToMany(mappedBy = "commissionedDoctor") // 의뢰받은 진료요청서
    private List<ServiceRequestEntity> referralServiceRequestEntity = new ArrayList<>();

    public PractitionerEntity(Practitioner practitioner, OrganizationEntity organization) {
        this.setName(practitioner.getName().get(0).getText());
        this.setLicenseNumber(practitioner.getIdentifier().stream().filter(identifier -> "LicenseNumber".equals(identifier.getSystem())).map(Identifier::getValue).findFirst().get());
        this.setOrganizationEntity(organization);
        this.contact = new ArrayList<>();
        this.commissionedServiceRequestEntity = new ArrayList<>();
        this.referralServiceRequestEntity = new ArrayList<>();
    }

    public Practitioner translateToPractitionerResource() {
        Practitioner practitioner = new Practitioner();

        practitioner.addName(new HumanName().setText(this.name));
        practitioner.addIdentifier(new Identifier().setValue("LicenseNumber").setValue(licenseNumber));
        practitioner.addIdentifier(new Identifier().setSystem("ID").setValue(this.id.toString()));

        this.contact
                .stream()
                .forEach(
                        each -> practitioner.addTelecom(new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(each))
                );

        return practitioner;
    }
}
