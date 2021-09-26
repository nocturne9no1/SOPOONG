package com.sopoong.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.followRequest;
import com.sopoong.model.entity.Relation;
import com.sopoong.service.RelationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
//@RequestMapping("/api/auth")
public class RelationController {
	
	@Autowired
	private RelationService relationService;
	
	@PostMapping("follow")
	@ApiOperation("팔로우 및 취소")
	public ResponseEntity<Relation> follow(@RequestBody followRequest request) throws Exception {
		BaseMessage bm= relationService.follow(request);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
		
	}
	
	@GetMapping("follow/followingList")
	@ApiOperation("내가 팔로우 한 리스트")
	public ResponseEntity getFollowingList(@RequestParam final String relationFollowing, final Pageable pageable) throws Exception {
		
		BaseMessage bm= relationService.getFollowingList(pageable, relationFollowing);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
		
	}
	
	@GetMapping("follow/followedList")
	@ApiOperation("나를 팔로우 하는 리스트")
	public ResponseEntity getFollowedList(@RequestParam final String relationFollowed, final Pageable pageable) throws Exception {
		
		BaseMessage bm= relationService.getFollowedList(pageable, relationFollowed);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
		
	}
	
}
