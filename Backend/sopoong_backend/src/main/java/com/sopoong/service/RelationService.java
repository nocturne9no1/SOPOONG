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
import com.sopoong.model.dto.FollowListRequest;
import com.sopoong.model.dto.followRequest;
import com.sopoong.model.entity.Alarm;
import com.sopoong.model.entity.Relation;
import com.sopoong.repository.AlarmRepository;
import com.sopoong.repository.RelationRepository;
import com.sopoong.repository.UserRepository;

@Service
public class RelationService {
	
	@Autowired
	private RelationRepository relationRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AlarmRepository alarmRepo;
	
	@Transactional
	public BaseMessage follow(followRequest request) {
		
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Relation> relation= relationRepo.findByRelationFollowingAndRelationFollowed(userRepo.findByUserId(request.getRelationFollowing()).get(), userRepo.findByUserId(request.getRelationFollowed()).get());
		
		if (relation.isPresent()) {
			relationRepo.delete(relation.get());
			resultMap.put("success", "팔로우 취소");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			long index= relationRepo.save(Relation.builder()
					.relationFollowing(userRepo.findByUserId(request.getRelationFollowing()).get())
					.relationFollowed(userRepo.findByUserId(request.getRelationFollowed()).get())
					.build()).getRelationIdx();
			
//			System.out.println(String.format("%03d", Integer.parseInt(Integer.toBinaryString(userRepo.findByUserId(request.getRelationFollowed()).get().getUserAlarm()))));
			if (String.format("%03d", Integer.parseInt(Integer.toBinaryString(userRepo.findByUserId(request.getRelationFollowed()).get().getUserAlarm()))).charAt(1)=='1') {
				alarmRepo.save(Alarm.builder()
					.user(userRepo.findByUserId(request.getRelationFollowed()).get())
					.alarmCategory(2)
					.alarmCheck(0)
					.relation(relationRepo.findById(index).get())
					.build());
			}
			
			resultMap.put("success", "팔로우 성공");
			return new BaseMessage(HttpStatus.OK, resultMap);
		}
		
	}

	
	@Transactional
	public BaseMessage getFollowingList(Pageable pageable, String relationFollowing) {
		
		Map<String,Object> resultMap= new HashMap<>();
		Page<Relation> relations= relationRepo.findByRelationFollowing(pageable, userRepo.findByUserId(relationFollowing).get());
		List<FollowListRequest> followingList= new ArrayList<>();
		
		for (int i=0; i<relations.getContent().size(); i++) {
			FollowListRequest request= new FollowListRequest();
			request.setUserIdx(relations.getContent().get(i).getRelationFollowed().getUserIdx());
			request.setUserNickname(relations.getContent().get(i).getRelationFollowed().getUserNickname());
			followingList.add(request);
		}
		
		resultMap.put("success", followingList);
		resultMap.put("isLast", relations.isLast());
		return new BaseMessage(HttpStatus.OK,resultMap);
		
	}
	
	@Transactional
	public BaseMessage getFollowedList(Pageable pageable, String relationFollowed) {
		
		Map<String,Object> resultMap= new HashMap<>();
		Page<Relation> relations= relationRepo.findByRelationFollowed(pageable, userRepo.findByUserId(relationFollowed).get());
		List<FollowListRequest> followedList= new ArrayList<>();
		
		for (int i=0; i<relations.getContent().size(); i++) {
			FollowListRequest request= new FollowListRequest();
			request.setUserIdx(relations.getContent().get(i).getRelationFollowing().getUserIdx());
			request.setUserNickname(relations.getContent().get(i).getRelationFollowing().getUserNickname());
			followedList.add(request);
		}
		
		resultMap.put("success", followedList);
		resultMap.put("isLast", relations.isLast());
		return new BaseMessage(HttpStatus.OK,resultMap);
		
	}
	
}
