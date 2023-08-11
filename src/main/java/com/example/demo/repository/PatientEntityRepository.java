package com.example.demo.repository;

import com.example.demo.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientEntityRepository extends JpaRepository<PatientEntity,Long> {

    public List<PatientEntity> findPatientEntitiesByName(String name);


    public List<PatientEntity> findPatientEntitiesByNameAndRRN(String name, String RRN);


    public Optional<PatientEntity> findPatientByNameAndRRN(String name, String RRN);
}
