package com.sopoong.model.dto;

import java.time.LocalDateTime;

import com.sopoong.model.entity.Travel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedDto {
	private Long travelIdx;
	private String userId;
	private String userNickname;
	private String travelTitle;
	private String travelContent;
	private String imagePath;
	private String imageTitle;
	private LocalDateTime createdAt;
	
	public FeedDto(Travel travel) {
		travelIdx = travel.getTravelIdx();
		userId = travel.getUser().getUserId();
		userNickname = travel.getUser().getUserNickname();
		travelTitle = travel.getTravelTitle();
		travelContent = travel.getTravelContent();
		imagePath = travel.getImage().getImagePath();
		imageTitle = travel.getImage().getImageOriginTitle();
		createdAt = travel.getCreatedAt();
	}
}
