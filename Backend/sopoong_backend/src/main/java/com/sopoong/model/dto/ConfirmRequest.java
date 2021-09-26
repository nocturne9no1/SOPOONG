package com.sopoong.model.dto;

import com.sopoong.model.entity.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRequest {
	private String id;
	private String authNumber;
}
