package com.example.demo.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;/*
import ca.uhn.fhir.rest.server.IResourceProvider;*/
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.example.demo.resourceProvider.PatientResourceProvider;
import com.example.demo.service.PatientService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientResourceProvider patientResourceProvider;

    @Autowired
    private FhirContext fhirContext;

    @GetMapping("/createPatient")
    public String createPatient() {
        List<HumanName> arrayList = new ArrayList<HumanName>();


        HumanName humanName = new HumanName();
        humanName.setText("dtd");
        arrayList.add(humanName);

        Patient patient = new Patient();
        patient.setName(arrayList);
        patient.setIdBase("1");
        patient.setGender(Enumerations.AdministrativeGender.MALE);

        FhirContext fhirContext = FhirContext.forR4();

        IParser iParser = fhirContext.newJsonParser()
                .setPrettyPrint(true);

        return iParser
                .encodeResourceToString(patient);
    }

    @PostMapping("/createPatient")
    public String createPatient(@RequestBody String body) {
        IParser jsonParser = fhirContext.newJsonParser();
        jsonParser.setParserErrorHandler(new StrictErrorHandler());

        Patient patient;
        MethodOutcome methodOutcome = new MethodOutcome();
        try {
            patient = jsonParser.parseResource(Patient.class, body);
        } catch (UnprocessableEntityException e) {
            OperationOutcome operationOutcome = new OperationOutcome();
            operationOutcome.addIssue().setDiagnostics(e.getMessage());
            methodOutcome.setOperationOutcome(operationOutcome);
            methodOutcome.setResponseStatusCode(422);

            String s = jsonParser.encodeResourceToString(methodOutcome.getResource());
            return s;
        }


        MethodOutcome methodOutcome1 = patientResourceProvider.createPatient(patient);
        String s = jsonParser.encodeResourceToString(methodOutcome1.getResource());

        return s;
    }
}

