package com.vanheurck.dpp.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vanheurck.dpp.entity.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
	Optional<Material> findByCode(String code);

    List<Material> findByIsActiveTrue();
}
