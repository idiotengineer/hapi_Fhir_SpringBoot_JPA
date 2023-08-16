package com.example.demo.repository;

import com.example.demo.entity.ServiceRequestEntity;
import com.example.demo.repository.Custom.ServiceRequestEntityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestEntityRepository extends JpaRepository<ServiceRequestEntity,Long>, ServiceRequestEntityRepositoryCustom {
}
