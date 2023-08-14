package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PatientEntity {

    @Id
    @GeneratedValue
    @Column(name = "patient_entity_id")
    private Long id;

    private String name;

    private String gender;

    private String RRN; // 주민등록번호

    @ElementCollection //핸드폰 번호
    @CollectionTable(name = "patient_phone_number_table")
    @Column(name = "phoneNumber")
    private List<String> phoneNumber = new ArrayList<>();

    @ElementCollection //주소
    @CollectionTable(name = "address_table")
    @Column(name = "address")
    private List<String> address = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<ConditionEntity> conditionEntity = new ArrayList<>();


    public Patient translateToPatientResource() {
        Patient patient = new Patient();

        patient.addIdentifier(new Identifier().setSystem("ID").setValue(id.toString()));
        patient.addAddress(new Address().setText(this.getRRN()));
        patient.addName(new HumanName().setText(this.getName()));

        this.phoneNumber
                .stream()
                .forEach(
                        each -> {
                            Patient.ContactComponent contactComponent = new Patient.ContactComponent();
                            contactComponent.addTelecom(
                                    new ContactPoint()
                                            .setSystem(ContactPoint.ContactPointSystem.PHONE)
                                            .setValue(each)
                            );
                        }
                );

        this.address
                .stream()
                .forEach(
                    each -> {
                        patient.addAddress(new Address().setText(each));
                    }
                );

        if (this.gender.toUpperCase().equals("MALE")) {
            patient.setGender(Enumerations.AdministrativeGender.MALE);
        } else {
            patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        }

        return patient;
    }
}
