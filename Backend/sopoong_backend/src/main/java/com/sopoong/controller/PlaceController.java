package com.sopoong.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.changePlaceRequest;
import com.sopoong.service.PlaceService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
public class PlaceController {
public static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private PlaceService placeService;
	
	@ApiOperation(value = "위치 일지 삭제")
	@DeleteMapping("/place/delete") 
	public ResponseEntity<BaseMessage> deletePlace(@RequestParam long placeIdx) {
		BaseMessage bm = placeService.deletePlace(placeIdx);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	}
	
	@ApiOperation(value = "위치 일지 수정")
	@PatchMapping("/place/update") 
	public ResponseEntity<BaseMessage> updatePlace(@RequestBody changePlaceRequest place) {
		BaseMessage bm = placeService.updatePlace(place);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	}
}
