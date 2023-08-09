package com.example.demo.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ca.uhn.fhir.rest.server.RestfulServer;
import java.io.IOException;

public class FhirServlet extends HttpServlet {
    private final CustomRestfulServlet customRestfulServlet;

    public FhirServlet(CustomRestfulServlet customRestfulServlet) {
        this.customRestfulServlet = customRestfulServlet;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        customRestfulServlet.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        customRestfulServlet.service(req, resp);
    }
}