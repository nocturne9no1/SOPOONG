package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class changePasswordRequest {
	
	String userId;
	
	String userEmail;
	
	String userPassword;
	
    String changedPassword;
	
}
