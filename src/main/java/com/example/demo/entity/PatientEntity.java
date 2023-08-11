package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.Patient;

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

    private String Gender;

    private String RRN; // 주민등록번호

    @ElementCollection //핸드폰 번호
    @CollectionTable(name = "phone_number_table")
    @Column(name = "phoneNumber")
    private List<String> phoneNumber = new ArrayList<>();

    @ElementCollection //주소
    @CollectionTable(name = "address_table")
    @Column(name = "address")
    private List<String> address = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<ConditionEntity> conditionEntity = new ArrayList<>();
}
