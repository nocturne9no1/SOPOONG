package com.sopoong.model.dto;

import org.springframework.web.multipart.MultipartFile;

import com.sopoong.model.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class getProfileRequest {
	
	String userId;
	
	String userEmail;
	
	String userNickname;
	
	int userIsVisible;
	
	int userAlarm;
	
	String userComment;
	
	String imageOriginTitle;
	
	long followings;
	
	long followers;
}
