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

			resultMap.put("success", "Place 대표 사진 등록 완료");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place 변경 실패 (존재하지 않는 placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage deletePlace(long placeIdx) { 
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Place> delPlace= placeRepository.findByplaceIdx(placeIdx);
		List<Image> delImageList = imageRepository.findByPlace_PlaceIdx(placeIdx); // 삭제될 이미지 리스트
		
		// Travel 대표 이미지 구하기
		long travelIdx = delPlace.get().getTravel().getTravelIdx();
		Optional<Travel> travel = travelRepository.findBytravelIdx(travelIdx);
		long travelImageIdx = travel.get().getImage().getImageIdx();
		
		if (delPlace.isPresent()) {
			// 삭제될 이미지 검사
			for(Image i : delImageList) {
				if(i.getImageIdx() == travelImageIdx) { // 삭제될 이미지가 travel의 대표 이미지인 경우
					Image im = Image.builder()
							.imageIdx(1)
							.imageLat(i.getImageLat())
							.imageLong(i.getImageLong())
							.build();
					
					travelService.updateTravel(travel.get(), im); // Travel의 image_idx = 1로 변경
				}
				
				// 파일 삭제
				String path = i.getImagePath();
				File file = new File(path);
				if(file.delete()){ // 파일 삭제에 성공하면 true, 실패하면 false
//	                System.out.println("파일을 삭제하였습니다");
	            }else{
//	                System.out.println("파일 삭제에 실패하였습니다");
	            }
			}
			
			placeRepository.delete(delPlace.get());
			
			resultMap.put("success", "Place 삭제 성공");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place 삭제 실패 (존재하지 않는 placeIdx)");
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
			resultMap.put("success", "Place 수정 성공");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place 수정 실패 (존재하지 않는 placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}
}
