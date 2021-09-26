package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowListRequest {
	
	long userIdx;
	String userNickname;
	
}
