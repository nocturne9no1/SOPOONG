package com.sopoong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.ScrapRequest;
import com.sopoong.service.ScrapService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
public class ScrapController {
	@Autowired
	private ScrapService scrapService;
	@GetMapping("scrap")
	@ApiOperation("스크랩게시글 가져오기")
	public ResponseEntity getScraps(@RequestParam(required = true) final String id, @PageableDefault(sort = {"createdAt"},direction = Direction.DESC) Pageable pageable) throws Exception{
		BaseMessage bm = scrapService.getScraps(id,pageable);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
	}
	
	@PostMapping("scrap")
	@ApiOperation("스크랩 저장")
	public ResponseEntity saveScraps(@RequestBody final ScrapRequest scrapRequest) throws Exception{
		BaseMessage bm = scrapService.scrap(scrapRequest);
		return new ResponseEntity(new BaseMessage(bm.getHttpStatus(),bm.getData()),bm.getHeaders(),bm.getHttpStatus());
	}
}
