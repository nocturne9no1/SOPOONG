package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRequest {
	
	Long alarmIdx;
	String userNickname;
	int alarmCategory;
	int alarmCheck;
	Long connectIdx;
	String travelTitle;
	
}
