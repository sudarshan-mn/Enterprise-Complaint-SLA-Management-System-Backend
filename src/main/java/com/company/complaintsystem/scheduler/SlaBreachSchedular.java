package com.company.complaintsystem.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.company.complaintsystem.entity.Complaint;
import com.company.complaintsystem.entity.ComplaintHistory;
import com.company.complaintsystem.entity.ComplaintStatus;
import com.company.complaintsystem.repository.ComplaintHistoryRepository;
import com.company.complaintsystem.repository.ComplaintRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class SlaBreachSchedular {

	
	private final ComplaintRepository complaintRepository;
	private final ComplaintHistoryRepository complaintHistoryRepository;
	
	@Scheduled(fixedRate=300000)
	public void detectSlaBreach() {
		
		List<Complaint> overdueComplaints=complaintRepository.findByStatusAndSlaDeadlineBefore(ComplaintStatus.IN_PROGRESS,
				LocalDateTime.now());
		
		
		for(Complaint complaint:overdueComplaints) {
			
			if(!complaint.isSlaBreached()) {
				
				ComplaintStatus oldStatus=complaint.getStatus();
				complaint.setSlaBreached(true);
				
				complaint.setPriority(complaint.getPriority().escalate());
				
				complaintRepository.save(complaint);
				
				ComplaintHistory history=ComplaintHistory.builder()
						.complaint(complaint)
						.oldStatus(oldStatus)
						.newStatus(oldStatus)
						.changedAt(LocalDateTime.now())
						.build();
				
				complaintHistoryRepository.save(history);
			}
			
		}
	}
}
