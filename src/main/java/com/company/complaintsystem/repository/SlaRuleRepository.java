package com.company.complaintsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.complaintsystem.entity.ComplaintCategory;
import com.company.complaintsystem.entity.SlaRule;

public interface SlaRuleRepository extends JpaRepository<SlaRule,Long> {

	
	Optional<SlaRule> findByCategory(ComplaintCategory category);
}
