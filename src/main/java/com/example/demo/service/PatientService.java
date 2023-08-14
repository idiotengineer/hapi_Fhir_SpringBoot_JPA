package com.example.demo.service;

import com.example.demo.entity.PatientEntity;
import com.example.demo.repository.PatientEntityRepository;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientEntityRepository patientEntityRepository;

    public PatientEntity getPatientEntityWithPatient(Patient patient) {
        PatientEntity patientEntity = new PatientEntity();

        // Convert HumanName to String and set it in PatientEntity
        String name = patient.getName().get(0).getText();
        patientEntity.setName(name);

        patientEntity.setGender(patient.getGender().toString());

        if (patient.hasIdentifier()) {
            List<Identifier> identifiers = patient.getIdentifier();

            Optional<String> RRNValue = identifiers
                    .stream()
                    .filter(identifier -> "RRN".equals(identifier.getSystem()))
                    .map(Identifier::getValue)
                    .findFirst();

            if (RRNValue.isPresent()) {
                patientEntity.setRRN(RRNValue.get());
            }
        }

        List<String> phoneNumbers = new ArrayList<>();
        List<String> addresslist = new ArrayList<>();

        patient.getTelecom()
                .stream()
                .forEach(
                        phoneNumber ->  phoneNumbers.add(phoneNumber.getValue())
                );
        patientEntity.setPhoneNumber(phoneNumbers);


        patient.getAddress()
                .stream()
                .forEach(
                        address -> addresslist.add(address.getText())
                );
        patientEntity.setAddress(addresslist);
        return patientEntity;
    }

    public PatientEntity createPatient(Patient patient) {
        PatientEntity patientEntity = getPatientEntityWithPatient(patient);

        PatientEntity savedPatient = patientEntityRepository.save(patientEntity);
        return savedPatient;
    }


    public Optional<PatientEntity> readPatient(IdType idType) {
        return patientEntityRepository.findById(idType.getIdPartAsLong());
    }

    public List<PatientEntity> searchPatient(String name) {
        return patientEntityRepository.findPatientEntitiesByName(name);
    }


    public Optional<PatientEntity> searchPatientByNameAndRRN(String name, String residentRegistrationNumber) {
        return patientEntityRepository.findPatientByNameAndRRN(name,residentRegistrationNumber);
    }

    public PatientEntity createOrSearchPatient(Patient parsedPatient) {
        Optional<PatientEntity> searchPatientEntity = patientEntityRepository
                .findPatientByNameAndRRN(
                        parsedPatient
                                .getName()
                                .get(0)
                                .getText(),
                        parsedPatient
                                .getIdentifier()
                                .stream()
                                .filter(identifier -> "RRN".equals(identifier.getSystem()))
                                .map(Identifier::getValue).findFirst().get());

        if (searchPatientEntity.isEmpty()) {
            return createPatient(parsedPatient);
        } else{
            return searchPatientEntity.get();
        }
    }
}
