package com.sopoong.model.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class TravelDetail {
	private TravelList travel;
	private ArrayList<PlaceList> placeList;
}
