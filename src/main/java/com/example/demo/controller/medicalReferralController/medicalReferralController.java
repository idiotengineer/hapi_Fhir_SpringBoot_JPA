package com.example.demo.controller.medicalReferralController;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import com.example.demo.entity.*;
import com.example.demo.resourceProvider.ConditionResourceProvider;
import com.example.demo.resourceProvider.OrganizationResourceProvider;
import com.example.demo.resourceProvider.PatientResourceProvider;
import com.example.demo.resourceProvider.PractitionerResourceProvider;
import com.example.demo.service.*;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
public class medicalReferralController {
/*
    @Autowired
    private PatientResourceProvider patientResourceProvider;

    @Autowired
    private ConditionResourceProvider conditionResourceProvider;

    @Autowired
    private OrganizationResourceProvider organizationResourceProvider;

    @Autowired
    private PractitionerResourceProvider practitionerResourceProvider;*/

    @Autowired
    private PractitionerService practitionerService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private CoverageService coverageService;

    @Autowired
    FhirContext fhirContext;

    @PostMapping("/medicalReferralPOSTRequest")
    public String createMedicalReferral(
            @RequestBody String bundleString
    ) {
       /* IParser iParser = fhirContext.newJsonParser().setPrettyPrint(true);

        Patient parsedPatient = iParser.parseResource(Patient.class, s);
        Condition parsedCondition = iParser.parseResource(Condition.class,s);
        Organization organization1 = iParser.parseResource(Organization.class, s);
        Practitioner practitioner1 = iParser.parseResource(Practitioner.class, s);
        Organization organization2 = iParser.parseResource(Organization.class, s);
        Practitioner practitioner2 = iParser.parseResource(Practitioner.class, s);

        *//*Patient generatedPatient = findOrCreatePatient(parsedPatient);
        Condition generatedCondition = (Condition) conditionResourceProvider.createCondition(parsedCondition, parsedPatient).getResource();
        Organization generatedOrganization1 = (Organization) organizationResourceProvider.createOrganization(organization1).getResource();
        Organization generatedOrganization2 = (Organization) organizationResourceProvider.createOrganization(organization1).getResource();
        MethodOutcome generatedPractitioner1 = practitionerResourceProvider.createPractitioner(practitioner1, organization1);
        MethodOutcome generatedPractitioner2 = practitionerResourceProvider.createPractitioner(practitioner2, organization2);*//*
         */
        IParser iParser = fhirContext.newJsonParser().setPrettyPrint(true);
        Bundle bundle = iParser.parseResource(Bundle.class, bundleString);

        Patient parsedPatient = null;
        Condition parsedCondition = null;
        Organization organization1 = null;
        Organization organization2 = null;
        Practitioner practitioner1 = null;
        Practitioner practitioner2 = null;
        Coverage parsedCoverage = null;
        int organizationCount = 0;
        int practitionerCount = 0;

        for (Bundle.BundleEntryComponent entry : bundle.getEntry()) {
            Resource resource = entry.getResource();

            if (resource instanceof Patient) {
                parsedPatient = (Patient) resource;
            } else if (resource instanceof Condition) {
                parsedCondition = (Condition) resource;
            } else if (resource instanceof Organization) {
                organizationCount++;

                if (organizationCount == 1) {
                    organization1 = (Organization) resource;
                } else if (organizationCount == 2) {
                    organization2 = (Organization) resource;
                }
            } else if (resource instanceof Practitioner) {
                practitionerCount++;

                if (practitionerCount == 1) {
                    practitioner1 = (Practitioner) resource;
                } else if (practitionerCount == 2) {
                    practitioner2 = (Practitioner) resource;
                }
            } else if (resource instanceof Coverage) {
                parsedCoverage = (Coverage) resource;
            }
        }

        PatientEntity patientEntity = patientService.createOrSearchPatient(parsedPatient);
        ConditionEntity conditionEntity = conditionService.createConditionEntityByPatientEntity(parsedCondition, patientEntity);
        OrganizationEntity organizationEntity1 = organizationService.createOrSearchByNameAndHospitalName(organization1);
        OrganizationEntity organizationEntity2 = organizationService.createOrSearchByNameAndHospitalName(organization2);
        PractitionerEntity practitionerEntity1 = practitionerService.createPractitionerEntity(practitioner1, organizationEntity1);
        PractitionerEntity practitionerEntity2 = practitionerService.createPractitionerEntity(practitioner2, organizationEntity2);
        CoverageEntity coverageEntity = coverageService.CreateCoverageEntity(parsedCoverage, patientEntity);
        ServiceRequestEntity serviceRequestEntity = serviceRequestService.createServiceRequestEntity(
                patientEntity,
                conditionEntity,
                organizationEntity1,
                practitionerEntity1,
                organizationEntity2,
                practitionerEntity2,
                coverageEntity
        );

        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.PROPOSAL);
        serviceRequest.setSubject(new Reference("Patient/" + patientEntity.getId().toString()));
        serviceRequest.setRequester(new Reference("Practitioner/" + practitionerEntity1.getId()));
        serviceRequest.setPerformer(Arrays.asList(new Reference("Practitioner/" + practitionerEntity2.getId())));
        serviceRequest.addReasonCode().setText(conditionEntity.getPatientCondition());
        serviceRequest.setOccurrence(new DateTimeType(serviceRequestEntity.getOccurrence()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Requester Organization ID").setValue(organizationEntity1.getId().toString()));
        serviceRequest.addIdentifier(new Identifier().setSystem("Performer Organization ID").setValue(organizationEntity2.getId().toString()));
        serviceRequest.addExtension(new Extension().setUrl("coverage-Type").setValue(new Reference(parsedCoverage)));

        return iParser.encodeResourceToString(serviceRequest);
    }

    @GetMapping("/medicalReferralGETRequest")
    public String readMedicalReferral(
            @RequiredParam(name = "patientName") String patientName,
            @RequiredParam(name = "commissionedDoctorName") String commissionedDoctorName,
            @RequiredParam(name = "referralDoctorName") String referralDoctorName
            ) {
        ServiceRequest serviceRequest = serviceRequestService.readServiceRequestEntity(patientName, commissionedDoctorName, referralDoctorName);

        return fhirContext.newJsonParser()
                .setPrettyPrint(true)
                .encodeResourceToString(serviceRequest);
    }

/*
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
    }*/
}
