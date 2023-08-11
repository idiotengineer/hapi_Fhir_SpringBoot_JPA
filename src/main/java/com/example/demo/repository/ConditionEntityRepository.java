package com.example.demo.repository;

import com.example.demo.entity.ConditionEntity;
import com.example.demo.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionEntityRepository extends JpaRepository<ConditionEntity, Long> {

    public List<ConditionEntity> findConditionEntitiesByPatient(PatientEntity patientEntity);
}
