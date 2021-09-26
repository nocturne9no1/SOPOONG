package com.sopoong.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class changeTravelRequest {
	private long travelIdx;
	private String travelTitle;
	private String travelContent;
	private int travelIsVisible;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
