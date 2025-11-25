package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.Certification;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    // supplier → certifications
    List<Certification> findBySupplierId(Long supplierId);
}