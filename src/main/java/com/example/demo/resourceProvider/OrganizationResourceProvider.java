package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.service.OrganizationService;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Optional;

@Configuration
public class OrganizationResourceProvider implements IResourceProvider {
    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Autowired
    OrganizationService organizationService;

    @Create
    public MethodOutcome createOrganization(@ResourceParam Organization organization) {
        OrganizationEntity organizationEntity = organizationService.createOrSearchByNameAndHospitalName(organization);

        MethodOutcome methodOutcome = new MethodOutcome();

        methodOutcome.setResource(organization);
        methodOutcome.setCreated(true);
        methodOutcome.setResponseStatusCode(201);

        return methodOutcome;
    }
}
