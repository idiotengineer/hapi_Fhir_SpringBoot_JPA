package com.example.demo.resourceProvider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.IValidatorModule;
import com.example.demo.entity.PatientEntity;
import com.example.demo.service.PatientService;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
public class PatientResourceProvider implements IResourceProvider {

    @Autowired
    private PatientService patientService;

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Autowired
    public FhirContext fhirContext;

    @Read(type = Patient.class)
    public Patient getResourceById(@IdParam IdType theId) {
        IParser iParser = fhirContext.newJsonParser().setPrettyPrint(true);

        Optional<PatientEntity> patientEntity = patientService.readPatient(theId);

        if (patientEntity.isEmpty()) {
            ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("리소스 조회 실패");
            throw resourceNotFoundException;
        }

        PatientEntity patientEntity1 = patientEntity.get();
        HumanName humanName = new HumanName();
        humanName.setText(patientEntity1.getName());

        Patient patient = new Patient();
        patient.setId(patientEntity1.getId().toString());
        patient.addName(humanName);

        return patient;
    }


    @Search
    public List<Patient> getPatient(@RequiredParam(name = Patient.SP_NAME) StringParam theFamilyName) {
        List<PatientEntity> patientEntities = patientService.searchPatient(theFamilyName.getValue());

        return patientEntities
                .stream()
                .map(
                        patientEntity -> {
                            Patient patient = new Patient();

                            patient.setId(patientEntity.getId().toString());
                            patient.setGender(
                                    patientEntity.getGender().equals("MALE") ? Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE
                            );
                            patient.addName().setText(patientEntity.getName());

                            return patient;
                        }
                )
                .collect(Collectors.toList());
    }

    @Create
    public MethodOutcome createPatient(@ResourceParam Patient patient) {
        FhirValidator fhirValidator = fhirContext.newValidator();
        IValidatorModule module = new FhirInstanceValidator(fhirContext);
        fhirValidator.registerValidatorModule(module);

        Meta meta = new Meta();
        Meta meta1 = meta.setLastUpdated(new Date());
        patient.setMeta(meta1);

        MethodOutcome methodOutcome = new MethodOutcome();
        PatientEntity patient1 = patientService.createPatient(patient);

        methodOutcome.setResource(patient);
        methodOutcome.setCreated(true);
        methodOutcome.setResponseStatusCode(201);

        return methodOutcome;
    }


    @Search
    public List<Patient> getPatientByNameAndRRN(
            @RequiredParam(name = Patient.SP_NAME) StringParam theFamilyName,
            @RequiredParam(name = Patient.SP_IDENTIFIER) StringParam stringParam
    ) {
        Optional<PatientEntity> patientEntity = patientService.searchPatientByNameAndRRN(theFamilyName.getValue(), stringParam.getValue());

        ArrayList<Patient> patients = new ArrayList<Patient>();

        Patient patient = new Patient();
        patient.setId(patientEntity.get().getId().toString());
        patient.setGender(
                patientEntity.get().getGender().equals("MALE") ? Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE
        );
        patient.addName().setText(patientEntity.get().getName());

        patientEntity.get().getPhoneNumber()
                        .stream()
                                .forEach(
                                        phonenumber -> patient.addTelecom(new ContactPoint().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(phonenumber))
                                );

        Identifier identifier = new Identifier();
        identifier.setSystem("RRN");
        identifier.setValue(patientEntity.get().getRRN());

        patient.addIdentifier(identifier);
        patients.add(patient);
        return patients;
    }
}
