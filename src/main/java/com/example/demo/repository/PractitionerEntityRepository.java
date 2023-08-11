package com.example.demo.repository;

import com.example.demo.entity.PractitionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PractitionerEntityRepository extends JpaRepository<PractitionerEntity, Long> {
}
