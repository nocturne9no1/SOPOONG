package com.sopoong.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceList {
	private long placeIdx;
	private String placeTitle;
	private String placeComment;
	private PlaceCategory placeCategory;
	private PlaceRate placeRates;
	private String placeTransport;
	private LocalDateTime placeVisitDate;
	private double placeLat;
	private double placeLong;
	private String imageOriginTitle;
	private int imageWidth;
	private int imageHeight;
}
