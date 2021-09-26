package com.sopoong.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.model.entity.User;
import com.sopoong.repository.AlarmRepository;
import com.sopoong.repository.GoodRepository;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.PlaceRepository;
import com.sopoong.repository.RelationRepository;
import com.sopoong.repository.ScrapRepository;
import com.sopoong.repository.TravelRepository;
import com.sopoong.repository.UserRepository;

import io.swagger.annotations.ApiOperation;


@RestController
public class TestController {
	public static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TravelRepository travelRepository;
	
	@Autowired
	ScrapRepository scrapRepository;
	
	@Autowired
	RelationRepository relationRepository;
	
	@Autowired
	PlaceRepository placeRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	GoodRepository goodRepository;
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@PostMapping("/user/test")
	@ApiOperation("테스트")
	public Object test() {
		ResponseEntity response = null;
		List<User> list = userRepository.findAll();
		logger.info(list.get(0).toString());
		return response;
	}
}
