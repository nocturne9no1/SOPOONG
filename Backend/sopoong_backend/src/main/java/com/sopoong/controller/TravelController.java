package com.sopoong.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.TravelDetail;
import com.sopoong.model.dto.TravelDto;
import com.sopoong.model.dto.TravelList;
import com.sopoong.model.dto.changeTravelRequest;
import com.sopoong.service.TravelService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
public class TravelController {
	public static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private TravelService travelService;
	
	@ApiOperation(value = "여행 일지 작성")
	@PostMapping("/travel/create")
	public ResponseEntity<BaseMessage> writeTravel(@ModelAttribute TravelDto travelDto) throws Exception {
		BaseMessage bm = travelService.writeTravel(travelDto);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	}
	
	@ApiOperation(value = "여행 일지 가져오기")
	@GetMapping("/travel/travelList")
	public ResponseEntity<BaseMessage> selectTravelList(@RequestParam String userId) {
		BaseMessage bm = travelService.selctTravelList(userId);
		List<TravelList> travelList = (List<TravelList>) bm.getData();
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), travelList), bm.getHeaders(), bm.getHttpStatus());
	}
	
	@ApiOperation(value = "여행 세부일지(travel 내용 + placeList[]) 가져오기")
	@GetMapping("/travel/travelDetail")
	public ResponseEntity<BaseMessage> selectTravelDetail(@RequestParam long travelIdx) {
		BaseMessage bm = travelService.selectTravelDetail(travelIdx);
		TravelDetail travelDetail = (TravelDetail) bm.getData();
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), travelDetail), bm.getHeaders(), bm.getHttpStatus());
	}
	
	@ApiOperation(value = "여행 일지 삭제") 
	@DeleteMapping("/travel/delete")
	public ResponseEntity<BaseMessage> deleteTravel(@RequestParam long travelIdx) {
		BaseMessage bm = travelService.deleteTravel(travelIdx);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	}
	
	@ApiOperation(value = "여행 일지 수정") 
	@PatchMapping("/travel/update")
	public ResponseEntity<BaseMessage> updateTravel(@RequestBody changeTravelRequest travel) {
		BaseMessage bm = travelService.updateTravel(travel);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	}
}
