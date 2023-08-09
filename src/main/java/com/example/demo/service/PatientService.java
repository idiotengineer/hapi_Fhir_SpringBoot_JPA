package com.example.demo.service;

import com.example.demo.entity.PatientEntity;
import com.example.demo.repository.PatientRepository;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public PatientEntity createPatient(Patient patient) {
        PatientEntity patientEntity = new PatientEntity();

        // Convert HumanName to String and set it in PatientEntity
        String name = patient.getName().get(0).getGiven().get(0).getValue();
        patientEntity.setName(name);

        patientEntity.setGender(patient.getGender().toString());

        PatientEntity savedPatient = patientRepository.save(patientEntity);
        return savedPatient;
    }


    public Optional<PatientEntity> readPatient(IdType idType) {
        return patientRepository.findById(idType.getIdPartAsLong());
    }

    public List<PatientEntity> searchPatient(String name) {
        return patientRepository.findPatientEntitiesByName(name);
    }
}
