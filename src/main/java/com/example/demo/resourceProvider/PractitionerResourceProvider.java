package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.PractitionerEntity;
import com.example.demo.service.OrganizationService;
import com.example.demo.service.PractitionerService;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PractitionerResourceProvider implements IResourceProvider {
    @Override
    public Class<Practitioner> getResourceType() {
        return Practitioner.class;
    }

    @Autowired
    public OrganizationService organizationService;

    @Autowired
    public PractitionerService practitionerService;

    @Create
    public MethodOutcome createPractitioner(
            @ResourceParam Practitioner practitioner/*,
            @ResourceParam Organization organization*/
            ) {
/*        OrganizationEntity organizationEntity = organizationService.createOrSearchByNameAndHospitalName(organization);
        PractitionerEntity practitionerEntity = new PractitionerEntity(practitioner, organizationEntity);
        practitionerService.createPractitionerEntity(practitionerEntity);*/

        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setResource(practitioner);
        methodOutcome.setCreated(true);
        methodOutcome.setResponseStatusCode(201);

        return methodOutcome;
    }
}
