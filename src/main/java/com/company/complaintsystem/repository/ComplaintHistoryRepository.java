package com.company.complaintsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.complaintsystem.entity.Complaint;
import com.company.complaintsystem.entity.ComplaintHistory;


public interface ComplaintHistoryRepository extends JpaRepository<ComplaintHistory,Long>{

	
	List<ComplaintHistory> findByComplaintOrderByChangedAtDesc(Complaint complaint);
}
