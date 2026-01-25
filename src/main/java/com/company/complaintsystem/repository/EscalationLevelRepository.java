package com.company.complaintsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.complaintsystem.entity.EscalationLevel;

public interface EscalationLevelRepository extends JpaRepository<EscalationLevel,Long> {

	
	Optional<EscalationLevel> findBylevel(int level);
}
