package com.example.demo.repository;

import com.example.demo.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity,Long> {

    public List<PatientEntity> findPatientEntitiesByName(String name);
}
