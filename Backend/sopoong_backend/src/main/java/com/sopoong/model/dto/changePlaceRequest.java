package com.sopoong.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class changePlaceRequest {
	private long placeIdx;
	private String userId;
	private String title;
	private String comment;
	private PlaceCategory category;
	private PlaceRate rates;
	private String transport;
}
