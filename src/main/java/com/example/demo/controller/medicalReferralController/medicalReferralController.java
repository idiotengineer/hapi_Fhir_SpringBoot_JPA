package com.example.demo.controller.medicalReferralController;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import com.example.demo.entity.PatientEntity;
import com.example.demo.resourceProvider.ConditionResourceProvider;
import com.example.demo.resourceProvider.PatientResourceProvider;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
public class medicalReferralController {

    @Autowired
    private PatientResourceProvider patientResourceProvider;

    @Autowired
    private ConditionResourceProvider conditionResourceProvider;

    @Autowired
    private

    @Autowired
    FhirContext fhirContext;

    @PostMapping("/medicalReferralRequest")
    public String medicalReferralRequest(
            @RequestBody String s
    ) {
        IParser iParser = fhirContext.newJsonParser().setPrettyPrint(true);

        Patient parsedPatient = iParser.parseResource(Patient.class, s);
        Condition parsedCondition = iParser.parseResource(Condition.class,s);

        Patient patient = findOrCreatePatient(parsedPatient);
        Condition condition = (Condition) conditionResourceProvider.createCondition(parsedCondition, patient).getResource();

    }

    private Patient findOrCreatePatient(Patient patient) {
        StringParam nameParam = new StringParam(patient.getName().get(0).getText());
        StringParam RRNParam = new StringParam(patient.getIdentifier().stream().filter(identifier -> "RRN".equals(identifier.getSystem())).map(Identifier::getValue).findFirst().get());

        List<Patient> patientByNameAndRRN = patientResourceProvider.getPatientByNameAndRRN(nameParam, RRNParam);

        if (patientByNameAndRRN.isEmpty()) {
            MethodOutcome methodOutcome = patientResourceProvider.createPatient(patient);
            Patient patient1 = (Patient) methodOutcome.getResource();
            patientByNameAndRRN.add(patient1);
        }

         return patientByNameAndRRN.get(0);
    }
}
