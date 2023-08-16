package com.example.demo.repository.Custom;

import com.example.demo.entity.ServiceRequestEntity;

import java.util.List;

public interface ServiceRequestEntityRepositoryCustom {
    public ServiceRequestEntity readServiceRequestEntity(
            String patientName,
            String commissionedDoctorName,
            String referralDoctorName
    );
}
