package com.example.demo.entity;

import com.example.demo.entity.EnumClass.CoverageTypeClass;
import com.example.demo.exception.NoCoverageTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.CoverageCopayType;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CoverageEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Coverage.CoverageStatus coverageStatus;

    @Enumerated(EnumType.STRING)
    private CoverageTypeClass coverageTypeClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private PatientEntity patientEntity;

    public CoverageEntity(Coverage coverage, PatientEntity patientEntity1) {
        this.coverageStatus = coverage.getStatus();
        try {
            /*String coverageType = coverage.getType().getCoding()
                    .stream()
                    .filter(each -> each.getSystem().equals("coverage-kind"))
                    .map(Coding::getCode)
                    .findFirst()
                    .orElseThrow(() -> new NoCoverageTypeException("CoverageTypeClass에 대입할 수 없습니다"));
*/
            Type coverageType = coverage.getExtension()
                    .stream()
                    .filter(extension -> extension.getUrl().equals("coverage-kind"))
                    .map(Extension::getValue)
                    .findFirst()
                    .orElseThrow(() -> new NoCoverageTypeException("CoverageTypeClass에 대입할 수 없습니다"));

            switch (coverageType.toString()) {
                case "HealthInsurance" -> this.coverageTypeClass = CoverageTypeClass.HEALTHINSURANCE;
                case "SelfPay" -> this.coverageTypeClass = CoverageTypeClass.SELFPAY;
                case "MedicalAid" -> this.coverageTypeClass = CoverageTypeClass.MEDICALAID;
                case "healthInsurance" -> this.coverageTypeClass = CoverageTypeClass.INDUSTRIALACCIDENT;
            }
        } catch (NoCoverageTypeException e) {
            coverageTypeClass = null;
        }
        this.patientEntity = patientEntity1;
    }
}
