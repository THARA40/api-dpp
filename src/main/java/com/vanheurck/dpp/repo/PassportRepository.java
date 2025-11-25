package com.vanheurck.dpp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.Passport;

import java.util.List;
import java.util.Optional;

public interface PassportRepository extends JpaRepository<Passport, Long> {

    List<Passport> findByProductId(Long productId);

    Optional<Passport> findByProductIdAndVersionNo(Long productId, Integer versionNo);
}
