package com.sopoong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.service.AlarmService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
public class AlarmController {
	
	@Autowired
	private AlarmService alarmService;
	
	@GetMapping("alarm/list")
	@ApiOperation("알람 리스트 출력")
	public ResponseEntity getList(@RequestParam String userId, final Pageable pageable) throws Exception{
		
		BaseMessage bm = alarmService.getList(pageable,userId);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
		
	}
	
	@GetMapping("alarm")
	@ApiOperation("알람 확인")
	public ResponseEntity readAlarm(@RequestParam long alarmIdx) {
		
		BaseMessage bm= alarmService.readAlarm(alarmIdx);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
		
	}
	
}
