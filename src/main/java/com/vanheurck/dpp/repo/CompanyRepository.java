package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
