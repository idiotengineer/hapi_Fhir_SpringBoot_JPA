package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coverage;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoverageResourceProvider implements IResourceProvider {
    @Override
    public Class<Coverage> getResourceType() {
        return Coverage.class;
    }

    @Create
    public MethodOutcome createCoverage(@ResourceParam Coverage coverage) {
        MethodOutcome methodOutcome = new MethodOutcome();

        methodOutcome.setCreated(true);
        methodOutcome.setResource(new Coverage());
        methodOutcome.setResponseStatusCode(201);

        return methodOutcome;
    }
}
