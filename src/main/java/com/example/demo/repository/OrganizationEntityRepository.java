package com.example.demo.repository;

import com.example.demo.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationEntityRepository extends JpaRepository<OrganizationEntity, Long> {

    public Optional<OrganizationEntity> findOrganizationEntityByNameAndHospitalCode(String name, String HospitalCode);
}
