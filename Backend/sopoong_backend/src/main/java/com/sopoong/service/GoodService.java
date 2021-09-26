package com.sopoong.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.GoodRequest;
import com.sopoong.model.entity.Alarm;
import com.sopoong.model.entity.Good;
import com.sopoong.repository.AlarmRepository;
import com.sopoong.repository.GoodRepository;
import com.sopoong.repository.TravelRepository;
import com.sopoong.repository.UserRepository;

@Service
public class GoodService {
	@Autowired
	private GoodRepository goodRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TravelRepository travelRepository;
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	public static final Logger logger = LoggerFactory.getLogger(GoodService.class);
	@Transactional
	public BaseMessage getList(Pageable pageable, Long travelIdx) {
		Map<String,Object> resultMap = new HashMap<>();
		Page<Good> goods = goodRepository.findByTravel_TravelIdx(travelIdx, pageable);
		List<String> people = new ArrayList<>();
		for(int i=0;i<goods.getContent().size();i++) {
			people.add(goods.getContent().get(i).getUser().getUserNickname());
		}
		resultMap.put("seccess", people);
		resultMap.put("isLast", goods.isLast());
		return new BaseMessage(HttpStatus.OK,resultMap); 
	}
	
	@Transactional
	public BaseMessage saveGood(GoodRequest goodRequest) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<Good> good = goodRepository.findByUser_UserIdAndTravel_TravelIdx(goodRequest.getUserId(), goodRequest.getTravelIdx());
		if(!good.isPresent()) {
			long index= goodRepository.save(Good.builder().user(userRepository.findByUserId(goodRequest.getUserId()).get())
					.travel(travelRepository.findById(goodRequest.getTravelIdx()).get()).build()).getGoodIdx();
			if (String.format("%03d", Integer.parseInt(Integer.toBinaryString(userRepository.findByUserId(goodRequest.getUserId()).get().getUserAlarm()))).charAt(2)=='1') {
				alarmRepository.save(Alarm.builder()
					.user(travelRepository.findBytravelIdx(goodRequest.getTravelIdx()).get().getUser())
					.alarmCategory(1)
					.alarmCheck(0)
					.good(goodRepository.findById(index).get())
					.build());
			}
			resultMap.put("success", "좋아요 누르기 성공");
			return new BaseMessage(HttpStatus.OK,resultMap); 
		}else {
			goodRepository.delete(good.get());
			resultMap.put("success", "좋아요 삭제하기 성공");
			return new BaseMessage(HttpStatus.OK,resultMap); 
		}
	}
}
