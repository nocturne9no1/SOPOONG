package com.sopoong.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class changeProfileRequest {
	
	String userId;
	
	String userNickname;
	
	String userComment;
	
	MultipartFile image;
	
}
