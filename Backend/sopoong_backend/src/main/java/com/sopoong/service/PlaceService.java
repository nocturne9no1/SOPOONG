package com.sopoong.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.PlaceDto;
import com.sopoong.model.dto.changePlaceRequest;
import com.sopoong.model.entity.Image;
import com.sopoong.model.entity.Place;
import com.sopoong.model.entity.Travel;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.PlaceRepository;
import com.sopoong.repository.TravelRepository;

@Service
public class PlaceService {
	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private TravelRepository travelRepository;
	
	@Autowired
	private TravelService travelService;

	@Transactional
	public BaseMessage writePlace(PlaceDto placeDto, String userId) {
		Place place = Place.builder()
				.placeTitle(placeDto.getTitle())
				.placeCategory(placeDto.getCategory().getMain())
				.placeCategory2(placeDto.getCategory().getSub())
				.placeRate1(placeDto.getRates().getRate1())
				.placeRate2(placeDto.getRates().getRate2())
				.placeRate3(placeDto.getRates().getRate3())
				.placeTransport(placeDto.getTransport())
				.placeComment(placeDto.getComment())
				.image(null)
				.placeLat(0.0)
				.placeLong(0.0)
				.placeVisitDate(placeDto.getVisitDate())
				.build();

		place = placeRepository.save(place);
		// resultMap.put("success", place);
		return new BaseMessage(HttpStatus.OK, place.getPlaceIdx());
	}

	@Transactional
	public BaseMessage updatePlace(long placeIdx, long travelIdx, Image placeImage) {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<Place> updatePlace = placeRepository.findByplaceIdx(placeIdx);

		if (updatePlace.isPresent()) {
			updatePlace.get().setImage(imageRepository.findByImageIdx(placeImage.getImageIdx()).get());
			updatePlace.get().setPlaceLat(placeImage.getImageLat());
			updatePlace.get().setPlaceLong(placeImage.getImageLong());
			updatePlace.get().setPlaceVisitDate(placeImage.getImageTime());
			updatePlace.get().setTravel(travelRepository.findBytravelIdx(travelIdx).get());
			placeRepository.save(updatePlace.get());

			resultMap.put("success", "Place ?????? ?????? ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place ?????? ?????? (???????????? ?????? placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage deletePlace(long placeIdx) { 
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Place> delPlace= placeRepository.findByplaceIdx(placeIdx);
		List<Image> delImageList = imageRepository.findByPlace_PlaceIdx(placeIdx); // ????????? ????????? ?????????
		
		// Travel ?????? ????????? ?????????
		long travelIdx = delPlace.get().getTravel().getTravelIdx();
		Optional<Travel> travel = travelRepository.findBytravelIdx(travelIdx);
		long travelImageIdx = travel.get().getImage().getImageIdx();
		
		if (delPlace.isPresent()) {
			// ????????? ????????? ??????
			for(Image i : delImageList) {
				if(i.getImageIdx() == travelImageIdx) { // ????????? ???????????? travel??? ?????? ???????????? ??????
					Image im = Image.builder()
							.imageIdx(1)
							.imageLat(i.getImageLat())
							.imageLong(i.getImageLong())
							.build();
					
					travelService.updateTravel(travel.get(), im); // Travel??? image_idx = 1??? ??????
				}
				
				// ?????? ??????
				String path = i.getImagePath();
				File file = new File(path);
				if(file.delete()){ // ?????? ????????? ???????????? true, ???????????? false
//	                System.out.println("????????? ?????????????????????");
	            }else{
//	                System.out.println("?????? ????????? ?????????????????????");
	            }
			}
			
			placeRepository.delete(delPlace.get());
			
			resultMap.put("success", "Place ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place ?????? ?????? (???????????? ?????? placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage updatePlace(changePlaceRequest place) {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<Place> updatePlace = placeRepository.findByplaceIdx(place.getPlaceIdx());
		
		if(updatePlace.isPresent()) {
			updatePlace.get().setPlaceTitle(place.getTitle());
			updatePlace.get().setPlaceComment(place.getComment());
			updatePlace.get().setPlaceCategory(place.getCategory().getMain());
			updatePlace.get().setPlaceCategory2(place.getCategory().getSub());
			updatePlace.get().setPlaceRate1(place.getRates().getRate1());
			updatePlace.get().setPlaceRate2(place.getRates().getRate2());
			updatePlace.get().setPlaceRate3(place.getRates().getRate3());
			updatePlace.get().setPlaceTransport(place.getTransport());
			
			placeRepository.save(updatePlace.get());
			resultMap.put("success", "Place ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place ?????? ?????? (???????????? ?????? placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}
}
