package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PractitionerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String licenseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private OrganizationEntity organizationEntity;

    @OneToMany(mappedBy = "referralDoctor") // 의뢰한 진료요청서
    private List<ServiceRequestEntity> commissionedServiceRequestEntity = new ArrayList<>();

    @OneToMany(mappedBy = "commissionedDoctor") // 의뢰받은 진료요청서
    private List<ServiceRequestEntity> referralServiceRequestEntity = new ArrayList<>();
}
