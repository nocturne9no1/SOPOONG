package com.sopoong.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sopoong.common.BaseMessage;
import com.sopoong.service.ImageService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class ImageController {
	public static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private ImageService imageService;


/*	@PostMapping("/image/profile")
	@ApiOperation(value = "사진 등록")
	public ResponseEntity write(@RequestBody ProfileDto profile) throws IllegalStateException, NoSuchAlgorithmException, IOException {
		BaseMessage bm = imageService.saveProfile(image, "wpffl3333");
		return new ResponseEntity<BaseMessage>(new BaseMessage(bm.getHttpStatus(), bm.getData()), bm.getHeaders(), bm.getHttpStatus());
	} */
	
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
	@ApiOperation("이미지 불러오기")
	public @ResponseBody byte[] getImage(@PathVariable(name = "imageName") String imageName,
		HttpServletRequest request) throws IOException {
		String imagePath = "/image/"+imageName;
		InputStream imageStream;
		try {
			imageStream = new FileInputStream(imagePath);
		} catch (Exception e) {
			imageStream = new FileInputStream("/profile/"+imageName);
		}
		
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();

		return imageByteArray;
	}
	
}
