package com.company.complaintsystem.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardResponseDto {

    private long totalComplaints;

    private long newComplaints;
    private long assignedComplaints;
    private long inProgressComplaints;
    private long resolvedComplaints;
    private long closedComplaints;

    private long slaBreachedComplaints;

    private long engineersCount;
    private long leadsCount;
    private long managersCount;
}
