package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.example.demo.service.ServiceRequestService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceRequestResourceProvider implements IResourceProvider {
    @Override
    public Class<ServiceRequest> getResourceType() {
        return ServiceRequest.class;
    }

    @Autowired
    ServiceRequestService serviceRequestService;

    @Create
    public MethodOutcome createServiceRequestResource(@ResourceParam ServiceRequest request) {
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setResource(request);
        methodOutcome.setResponseStatusCode(201);
        methodOutcome.setCreated(true);

        return methodOutcome;
    }
}
