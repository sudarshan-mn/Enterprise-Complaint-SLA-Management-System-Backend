package com.company.complaintsystem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name="complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	
	@Column(columnDefinition="TEXT")
	private String description;
	
	@Enumerated(EnumType.STRING)
	private ComplaintCategory category;
	
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	
	@Enumerated(EnumType.STRING)
	private ComplaintStatus status;
	
	
	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	
	private LocalDateTime createdAt;
	
	private LocalDateTime resolvedAt;

	
	private LocalDateTime slaDeadline;
	
	@Column(name = "sla_breached")
	private boolean slaBreached;

	
	@ManyToOne
	@JoinColumn(name="assigned_to")
	private User assignedTo;
	
	
	
}
