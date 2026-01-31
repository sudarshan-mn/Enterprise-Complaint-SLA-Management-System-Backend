package com.company.complaintsystem.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.company.complaintsystem.entity.Complaint;
import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.entity.User;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {

	
	List<Complaint> findByAssignedTo(User user);
	
	List<Complaint> findByStatusAndSlaDeadlineBefore(
			ComplaintStatus status,
			LocalDateTime time);
	
	long countByStatus(ComplaintStatus status);
	long countBySlaBreachedTrue();
	
	List<Complaint>findBySlaBreachedTrue();
	
//	List<Complaint>findByCreatedByOrderByCreatedAtDesc(User user);
	
	Page<Complaint> findByCreatedByOrderByCreatedAtDesc(User user ,Pageable pageable );
	
	List<Complaint> findByStatus(ComplaintStatus status);

	

}
 