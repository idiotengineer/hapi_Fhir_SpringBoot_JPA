package com.example.demo.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import com.example.demo.resourceProvider.PatientResourceProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This servlet is the actual FHIR server itself
 */
@Component
public class CustomRestfulServlet extends RestfulServer {

    private final PatientResourceProvider patientResourceProvider;

    @Override
    public boolean canStoreSearchResults() {
        return super.canStoreSearchResults();
    }

    /**
     * Constructor
     */

    public CustomRestfulServlet(PatientResourceProvider patientResourceProvider) {
        super(FhirContext.forR4());
        this.patientResourceProvider = patientResourceProvider;
    }

    /**
     * This method is called automatically when the
     * servlet is initializing.
     */
    @Override
    public void initialize() {
        List<IResourceProvider> providers = new ArrayList<>();
        providers.add(this.patientResourceProvider);
        setResourceProviders(providers);

        INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
        getFhirContext().setNarrativeGenerator(narrativeGen);

        registerInterceptor(new ResponseHighlighterInterceptor());
    }
}