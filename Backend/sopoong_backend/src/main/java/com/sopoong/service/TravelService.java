package com.sopoong.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.FileDto;
import com.sopoong.model.dto.PlaceCategory;
import com.sopoong.model.dto.PlaceDto;
import com.sopoong.model.dto.PlaceList;
import com.sopoong.model.dto.PlaceRate;
import com.sopoong.model.dto.TravelDetail;
import com.sopoong.model.dto.TravelDto;
import com.sopoong.model.dto.TravelList;
import com.sopoong.model.dto.changeTravelRequest;
import com.sopoong.model.entity.Alarm;
import com.sopoong.model.entity.Image;
import com.sopoong.model.entity.Place;
import com.sopoong.model.entity.Relation;
import com.sopoong.model.entity.Travel;
import com.sopoong.model.entity.User;
import com.sopoong.repository.AlarmRepository;
import com.sopoong.repository.GoodRepository;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.PlaceRepository;
import com.sopoong.repository.RelationRepository;
import com.sopoong.repository.ScrapRepository;
import com.sopoong.repository.TravelRepository;
import com.sopoong.repository.UserRepository;

@Service
public class TravelService {

	@Autowired
	private TravelRepository travelRepository;
	
	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	@Autowired
	private RelationRepository relationRepository;

	@Autowired
	private GoodRepository goodRepository;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ScrapRepository scrapRepository;
	
	@Transactional
	public BaseMessage writeTravel(TravelDto travelDto) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();

		String userId = travelDto.getUserId();
		long travelIdx;
		long placeIdx;

		/* 1. 여행 기록 */
		Travel travel = Travel.builder()
				.user(userRepository.findByUserId(travelDto.getUserId()).get())
				.travelTitle(travelDto.getTravelTitle())
				.travelContent(travelDto.getTravelContent())
				.travelIsVisible(travelDto.getTravelIsVisible())
				.startDate(travelDto.getStartDate())
				.endDate(travelDto.getEndDate())
				.image(null)
				.travelLat(0.0)
				.travelLong(0.0)
				.startDate(travelDto.getStartDate())
				.endDate(travelDto.getEndDate())
				.build();

		travel = travelRepository.save(travel);
		resultMap.put("success", travel);

		// 여행 기록 idx 가져오기
		travelIdx = travel.getTravelIdx();

		/* 2. 위치 기록 */
		List<PlaceDto> places = travelDto.getPlaceList();
		for (PlaceDto place : places) {
			// 위치기록 저장
			BaseMessage bmPlace = placeService.writePlace(place, userId);
			if (!bmPlace.getHttpStatus().equals(HttpStatus.OK))
				return new BaseMessage(HttpStatus.BAD_REQUEST, bmPlace.getData());

			// 위치 기록 idx를 사진 테이블에 저장
			placeIdx = (long) bmPlace.getData();

			/* 3. 사진 기록 */
			List<FileDto> files = place.getImageList();
			BaseMessage bmImage = imageService.saveImage(files, userId, travelIdx, placeIdx);
			if (!bmImage.getHttpStatus().equals(HttpStatus.OK)) {
				return new BaseMessage(HttpStatus.BAD_REQUEST, bmImage.getData());
			}

			/* place 대표사진 idx 구하기 */
			Image placeImage = imageRepository.findImageByPlace_PlaceIdxAndIsPlaceLeader(placeIdx, 1).get();

			// place 테이블에 저장
			bmPlace = placeService.updatePlace(placeIdx, travelIdx, placeImage);
		}

		/* travel 대표사진 idx 구하기 */
		Image travelImage = imageRepository.findImageByTravel_TravelIdxAndIsTravelLeader(travelIdx, 1).get();

		// travel 테이블에 저장
		updateTravel(travel, travelImage);
		
		// 알람
		long index= travel.getTravelIdx();		
		List<Relation> followedList= userRepository.findByUserId(travelDto.getUserId()).get().getRelationFollowed();
		
		for (int i=0; i<followedList.size(); i++) {
			if (String.format("%03d", Integer.parseInt(Integer.toBinaryString(userRepository.findByUserId(followedList.get(i).getRelationFollowed().getUserId()).get().getUserAlarm()))).charAt(0)=='1') {
				alarmRepository.save(Alarm.builder()
						.user(followedList.get(i).getRelationFollowing())
						.alarmCategory(3)
						.alarmCheck(0)
						.travel(travelRepository.findById(index).get())
						.build());
			}
		}

		return new BaseMessage(HttpStatus.OK, "성공");
	}

	@Transactional
	protected BaseMessage updateTravel(Travel travel, Image travelImage) {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<Travel> updateTravel = travelRepository.findBytravelIdx(travel.getTravelIdx());
		
		if (updateTravel.isPresent()) {
			updateTravel.get().setImage(imageRepository.findByImageIdx(travelImage.getImageIdx()).get());
			updateTravel.get().setTravelLat(travelImage.getImageLat());
			updateTravel.get().setTravelLong(travelImage.getImageLong());

			travelRepository.save(updateTravel.get());
			resultMap.put("success", "Place 대표 사진 등록 완료");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Place 변경 실패 (존재하지 않는 placeIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage selctTravelList(String userId) {
		List<Travel> tlist = travelRepository.findByUser_UserId(userId);
		ArrayList<TravelList> travelList = new ArrayList<>();
		User loginUser = userRepository.findByUserId(userId).get();
		
		for (Travel travel : tlist) {
			Image image = travel.getImage();
			TravelList t = TravelList.builder()
						.travelIdx(travel.getTravelIdx())
						.travelTitle(travel.getTravelTitle())
						.travelContent(travel.getTravelContent())
						.imageOriginTitle(travel.getImage().getImageOriginTitle())
						.profileOriginTitle(travel.getUser().getImage().getImageOriginTitle())
						.travelLat(travel.getTravelLat())
						.travelLong(travel.getTravelLong())
						.startDate(travel.getStartDate())
						.endDate(travel.getEndDate())
						.imageWidth(travel.getImage().getImageWidth())
						.imageHeight(travel.getImage().getImageHeight())
						.totalLike(goodRepository.countByTravel_TravelIdx(travel.getTravelIdx()))
						.userId(travel.getUser().getUserId())
						.userNickname(travel.getUser().getUserNickname())
						.build();
			if(goodRepository.findByUser_UserIdAndTravel_TravelIdx(userId, travel.getTravelIdx()).isPresent()) t.setIsLike(1);
			else t.setIsLike(0);
			
			if(relationRepository.findByRelationFollowingAndRelationFollowed(userRepository.findByUserId(userId).get(),travel.getUser()).isPresent()) t.setIsFollow(1);
			else t.setIsFollow(0);
			
			if(scrapRepository.findByUser_UserIdAndTravel_TravelIdx(loginUser.getUserId(), travel.getTravelIdx()).isPresent()) t.setIsScrap(1);
			else t.setIsScrap(0);
			travelList.add(t);
		}
		return new BaseMessage(HttpStatus.OK, travelList);

	}

	public BaseMessage selectTravelDetail(long travelIdx) {
		Optional<Travel> travel = travelRepository.findBytravelIdx(travelIdx);
		
		// travelIdx에 해당하는 Travel 정보 저장
		TravelList t = TravelList.builder()
						.travelIdx(travel.get().getTravelIdx())
						.travelTitle(travel.get().getTravelTitle())
						.travelContent(travel.get().getTravelContent())
						.imageOriginTitle(travel.get().getImage().getImageOriginTitle())
						.travelLat(travel.get().getTravelLat())
						.travelLong(travel.get().getTravelLong())
						.startDate(travel.get().getStartDate())
						.endDate(travel.get().getEndDate())
						.imageWidth(travel.get().getImage().getImageWidth())
						.imageHeight(travel.get().getImage().getImageHeight())
						.totalLike(goodRepository.countByTravel_TravelIdx(travel.get().getTravelIdx()))
						.userId(travel.get().getUser().getUserId())
						.userNickname(travel.get().getUser().getUserNickname())
						.build();
		
		// PlaceList[] 정보 저장
		List<Place> plist = placeRepository.findByTravel_TravelIdx(travelIdx); // travelIdx로 검색한 placeList들
		ArrayList<PlaceList> placeList = new ArrayList<>();
		
		for(Place place : plist) {
			if (place == null) {
				return new BaseMessage(HttpStatus.BAD_REQUEST, "error: place is null");
			}
			
			PlaceList p = PlaceList.builder()
						.placeIdx(place.getPlaceIdx())
						.placeTitle(place.getPlaceTitle())
						.placeComment(place.getPlaceComment())
						.placeCategory(new PlaceCategory(place.getPlaceCategory(), place.getPlaceCategory2()))
						.placeRates(new PlaceRate(place.getPlaceRate1(), place.getPlaceRate2(), place.getPlaceRate3()))
						.placeTransport(place.getPlaceTransport())
						.placeVisitDate(place.getPlaceVisitDate())
						.placeLat(place.getPlaceLat())
						.placeLong(place.getPlaceLong())
						.imageWidth(place.getImage().getImageWidth())
						.imageHeight(place.getImage().getImageHeight())
						.imageOriginTitle(imageRepository.findByImageIdx(place.getImage().getImageIdx()).get().getImageOriginTitle())
						.build();
			
			placeList.add(p);
		}
		
		// TravelDetail에 데이터 저장
		TravelDetail travelDetail = TravelDetail.builder()
								.travel(t)
								.placeList(placeList)
								.build();
		return new BaseMessage(HttpStatus.OK, travelDetail);
	}

	public BaseMessage deleteTravel(long travelIdx) {
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Travel> delTravel= travelRepository.findBytravelIdx(travelIdx);
		
		List<Image> travelImgList = imageRepository.findByTravel_TravelIdx(travelIdx); // 삭제될 이미지
		
		if (delTravel.isPresent()) {
			// 삭제될 이미지들 파일 삭제
			for(Image i : travelImgList) {
				
				String path = i.getImagePath();
				File file = new File(path);
				
				if(file.delete()){ // 파일 삭제에 성공하면 true, 실패하면 false
//	                System.out.println("파일을 삭제하였습니다");
	            }else{
//	                System.out.println("파일 삭제에 실패하였습니다");
	            }
			}
			
			travelRepository.delete(delTravel.get()); 
			
			resultMap.put("success", "Travel 삭제 성공");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Travel 삭제 실패 (존재하지 않는 travelIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage updateTravel(changeTravelRequest travel) {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<Travel> updateTravel = travelRepository.findBytravelIdx(travel.getTravelIdx());
		
		if(updateTravel.isPresent()) {
			updateTravel.get().setTravelTitle(travel.getTravelTitle());
			updateTravel.get().setTravelContent(travel.getTravelContent());
			updateTravel.get().setTravelIsVisible(travel.getTravelIsVisible());
			updateTravel.get().setStartDate(travel.getStartDate());
			updateTravel.get().setEndDate(travel.getEndDate());
			
			travelRepository.save(updateTravel.get());
			resultMap.put("success", "Travel 수정 성공");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "Travel 수정 실패 (존재하지 않는 travelIdx)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}
}
