package com.company.complaintsystem.dto;

import com.company.complaintsystem.entity.ComplaintCategory;
import com.company.complaintsystem.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintCreateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private ComplaintCategory category;

    @NotNull
    private Priority priority;
}
