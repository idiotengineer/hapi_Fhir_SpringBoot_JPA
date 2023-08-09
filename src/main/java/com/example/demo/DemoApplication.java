package com.example.demo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.demo.config.CustomRestfulServlet;
import com.example.demo.config.FhirServlet;
import com.example.demo.resourceProvider.PatientResourceProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;


@SpringBootApplication
@ServletComponentScan(
		basePackages = "../java/com/example/demo/config"
)
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/*@Bean
	public FhirContext fhirContext() {
		FhirContext fhirContext = FhirContext.forR4();
		return fhirContext;
	}*/

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

	@Bean
	public ServletRegistrationBean<FhirServlet> servletRegistrationBean(CustomRestfulServlet customRestfulServlet) {
		ServletRegistrationBean<FhirServlet> bean = new ServletRegistrationBean<>(
				new FhirServlet(customRestfulServlet), "/fhir/*");
		bean.setLoadOnStartup(1);
		bean.setName("FhirServlet");
		return bean;
	}

	@Bean
	public PatientResourceProvider patientResourceProvider() {
		return new PatientResourceProvider();
	}

	@Bean
	public FhirContext getFhirContext() {
		return FhirContext.forR4();
	}
}
