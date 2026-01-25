package com.company.complaintsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="complaint_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintHistory {
		
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long Id;
		
		
		@ManyToOne
		@JoinColumn(name="complaint_id")
		private Complaint complaint;
		
		
		@Enumerated(EnumType.STRING)
		private ComplaintStatus oldStatus;
		
		
		@Enumerated(EnumType.STRING)
		private ComplaintStatus newStatus;
		
		@ManyToOne
		@JoinColumn(name="changed_by")
		private User changedBy;
		
		
		private  LocalDateTime changedAt;
}
