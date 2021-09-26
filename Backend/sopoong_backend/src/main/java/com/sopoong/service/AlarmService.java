package com.sopoong.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.AlarmRequest;
import com.sopoong.model.entity.Alarm;
import com.sopoong.repository.AlarmRepository;
import com.sopoong.repository.UserRepository;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepo;
	@Autowired
	private UserRepository userRepo;
	
	@Transactional
	public BaseMessage getList(Pageable pageable, String userId) {
		
		Map<String,Object> resultMap= new HashMap<>();
		Page<Alarm> alarms= alarmRepo.findByUser_UserId(userId,pageable);
		List<AlarmRequest> alarmList= new ArrayList<>();
		
		for (int i=0; i<alarms.getContent().size(); i++) {
			AlarmRequest alarm= new AlarmRequest();
			alarm.setAlarmIdx(alarms.getContent().get(i).getAlarmIdx());
			//alarm.setUserNickname(userRepo.findByUserId(userId).get().getUserNickname());
			alarm.setAlarmCategory(alarms.getContent().get(i).getAlarmCategory());
			alarm.setAlarmCheck(alarms.getContent().get(i).getAlarmCheck());
			if (alarms.getContent().get(i).getAlarmCategory()==1) {
				alarm.setUserNickname(alarms.getContent().get(i).getGood().getUser().getUserNickname());
				alarm.setConnectIdx(alarms.getContent().get(i).getGood().getTravel().getTravelIdx());
				alarm.setTravelTitle(alarms.getContent().get(i).getGood().getTravel().getTravelTitle());
			} else if (alarms.getContent().get(i).getAlarmCategory()==2) {
				alarm.setUserNickname(alarms.getContent().get(i).getRelation().getRelationFollowing().getUserNickname());
				alarm.setConnectIdx(alarms.getContent().get(i).getRelation().getRelationFollowing().getUserIdx());				
			} else if (alarms.getContent().get(i).getAlarmCategory()==3) {
				alarm.setUserNickname(alarms.getContent().get(i).getTravel().getUser().getUserNickname());
				alarm.setConnectIdx(alarms.getContent().get(i).getTravel().getTravelIdx());
				alarm.setTravelTitle(alarms.getContent().get(i).getTravel().getTravelTitle());
			}
			alarmList.add(alarm);
		}
		
		resultMap.put("success", alarmList);
		resultMap.put("isLast", alarms.isLast());
		return new BaseMessage(HttpStatus.OK,resultMap);
		
	}
	
	@Transactional
	public BaseMessage readAlarm(long alarmIdx) {
		
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Alarm> alarm= alarmRepo.findById(alarmIdx);
		
		if (alarm.get().getAlarmCheck()==0) {
			alarm.get().setAlarmCheck(1);
			resultMap.put("success", "알람 읽음");
		}
		
		return new BaseMessage(HttpStatus.OK,resultMap);
		
	}
	
}
