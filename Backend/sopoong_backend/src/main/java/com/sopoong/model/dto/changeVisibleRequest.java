package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class changeVisibleRequest {
	
	String userId;
	
	int userIsVisible;
	
}
