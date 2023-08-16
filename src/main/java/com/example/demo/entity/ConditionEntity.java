package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.VerificationResult;
import org.hl7.fhir.r4.model.codesystems.ConditionClinical;
import org.hl7.fhir.r4.model.codesystems.ConditionVerStatus;
import org.hl7.fhir.r4.model.codesystems.VerificationresultStatus;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConditionEntity {

    @Id
    @GeneratedValue
    @Column(name = "condition_entity_id")
    private Long id;

    private String patientCondition;

    @Enumerated(EnumType.STRING)
    private ConditionClinical conditionClinical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PatientEntity patient;

    @Enumerated(EnumType.STRING)
    private ConditionVerStatus conditionVerStatus;


    public ConditionEntity(Condition condition, PatientEntity patientEntity) {
        setConditionClinical(ConditionClinical.ACTIVE);
        setPatient(patientEntity);
        setConditionVerStatus(ConditionVerStatus.PROVISIONAL);
        setConditionClinical(ConditionClinical.ACTIVE);
        setPatientCondition(condition.getCode().getText());
    }
}
