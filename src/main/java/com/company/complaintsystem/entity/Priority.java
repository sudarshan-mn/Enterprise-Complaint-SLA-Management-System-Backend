package com.company.complaintsystem.entity;

public enum Priority {
			LOW,
			MEDIUM,
			HIGH;
			
			public Priority escalate() {
				return switch(this) {
				case LOW->MEDIUM;
				case MEDIUM->HIGH;
				case HIGH->HIGH;
				};
			}
}
