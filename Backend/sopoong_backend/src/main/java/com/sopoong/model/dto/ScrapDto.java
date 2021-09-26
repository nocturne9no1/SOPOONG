package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapDto {
	private long travelIdx;
	private String userId;
	private String userNickname;
	private String profileImagePath;
	private String travelTitle;
	private String travelComment;
	private String travelImagePath;
	private int travelIsVisible;
}
