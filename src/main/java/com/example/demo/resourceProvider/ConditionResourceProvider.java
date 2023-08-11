package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.demo.entity.ConditionEntity;
import com.example.demo.entity.PatientEntity;
import com.example.demo.service.ConditionService;
import com.example.demo.service.PatientService;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.codesystems.ConditionClinical;
import org.hl7.fhir.r4.model.codesystems.ConditionVerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConditionResourceProvider implements IResourceProvider {

    @Autowired
    ConditionService conditionService;

    @Autowired
    PatientService patientService;

    @Override
    public Class<Condition> getResourceType() {
        return Condition.class;
    }

    @Create
    public MethodOutcome createCondition(
            @ResourceParam Condition condition,
            @ResourceParam Patient patient) {
        ConditionEntity conditionEntity = new ConditionEntity();
        PatientEntity patientEntity = patientService.searchPatientByNameAndRRN(patient.getName().get(0).getText(), patient.getIdentifier().stream().filter(identifier -> "RRN".equals(identifier.getSystem())).map(Identifier::getValue).findFirst().get()).get();

        conditionEntity.setConditionClinical(ConditionClinical.ACTIVE);
        conditionEntity.setPatient(patientEntity);
        conditionEntity.setConditionVerStatus(ConditionVerStatus.PROVISIONAL);

        conditionService.createConditionEntity(conditionEntity);

        MethodOutcome methodOutcome = new MethodOutcome();

        methodOutcome.setResource(condition);
        methodOutcome.setCreated(true);
        methodOutcome.setResponseStatusCode(201);

        return methodOutcome;
    }
}
