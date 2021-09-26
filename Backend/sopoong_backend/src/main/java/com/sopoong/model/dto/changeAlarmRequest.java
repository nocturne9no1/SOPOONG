package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class changeAlarmRequest {
	
	String userId;
	
	int userAlarm;
	
}
