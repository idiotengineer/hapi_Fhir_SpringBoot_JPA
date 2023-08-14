package com.example.demo.service;

import com.example.demo.entity.OrganizationEntity;
import com.example.demo.repository.OrganizationEntityRepository;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    OrganizationEntityRepository organizationEntityRepository;

    public OrganizationEntity createOrSearchByNameAndHospitalName(Organization organization) {
        String name = organization.getName();
        String hospitalCode = organization.getIdentifier().stream().filter(identifier -> "HospitalCode".equals(identifier.getSystem())).map(Identifier::getValue).findFirst().get();

        Optional<OrganizationEntity> organizationEntityByNameAndHospitalCode = organizationEntityRepository.findOrganizationEntityByNameAndHospitalCode(name,hospitalCode);


        if (organizationEntityByNameAndHospitalCode.isEmpty()) {
            OrganizationEntity organizationEntity = new OrganizationEntity(organization);
            return organizationEntityRepository.save(organizationEntity);
        }

        return organizationEntityByNameAndHospitalCode.get();
    }
}
