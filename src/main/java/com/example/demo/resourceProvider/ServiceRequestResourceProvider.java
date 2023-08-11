package com.example.demo.resourceProvider;

import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceRequestResourceProvider implements IResourceProvider {
    @Override
    public Class<ServiceRequest> getResourceType() {
        return ServiceRequest.class;
    }
}
