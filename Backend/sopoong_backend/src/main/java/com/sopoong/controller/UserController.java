package com.sopoong.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.DeleteUserRequest;
import com.sopoong.model.dto.changeAlarmRequest;
import com.sopoong.model.dto.changePasswordRequest;
import com.sopoong.model.dto.changeProfileRequest;
import com.sopoong.model.dto.changeVisibleRequest;
import com.sopoong.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user")
	@ApiOperation(value = "프로필 가져오기")
	public ResponseEntity<BaseMessage> getProfile(@RequestParam String id) throws Exception {
		
		BaseMessage bm= userService.getProfile(id);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@PatchMapping("/user/profile")
	@ApiOperation(value = "프로필 변경하기")
	public ResponseEntity<BaseMessage> changeProfile(@ModelAttribute changeProfileRequest request) throws IllegalStateException, NoSuchAlgorithmException, IOException {
		
		BaseMessage bm= userService.changeProfile(request);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@PatchMapping("/user/password")
	@ApiOperation(value = "비밀번호 변경하기")
	public ResponseEntity<BaseMessage> changePassword(@Valid @RequestBody changePasswordRequest request) {
		
		BaseMessage bm= userService.changePassword(request);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@PatchMapping("/user/alarm")
	@ApiOperation(value = "알람 범위 변경하기")
	public ResponseEntity<BaseMessage> changeAlarm(@Valid @RequestBody changeAlarmRequest request) {
		
		BaseMessage bm= userService.changeAlarm(request);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@PatchMapping("/user/visible")
	@ApiOperation(value = "계정 공개범위 변경하기")
	public ResponseEntity<BaseMessage> changeVisible(@Valid @RequestBody changeVisibleRequest request) {
		
		BaseMessage bm= userService.changeVisible(request);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@DeleteMapping("/user/delete")
	@ApiOperation(value = "계정 삭제하기")
	public ResponseEntity<BaseMessage> deleteUser(@RequestBody DeleteUserRequest request) {
		
		BaseMessage bm= userService.deleteUser(request);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
	
	@GetMapping("/user/list")
	@ApiOperation(value = "아이디 포함 리스트 불러오기")
	public ResponseEntity<BaseMessage> getUserList(@RequestParam String id , Pageable pageable) {
		
		BaseMessage bm= userService.getUserList(id,pageable);
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
		
	}
}
