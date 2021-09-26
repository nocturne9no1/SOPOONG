package com.sopoong.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.FileDto;
import com.sopoong.model.entity.Image;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.PlaceRepository;
import com.sopoong.repository.TravelRepository;
import com.sopoong.repository.UserRepository;
import com.sopoong.util.MD5Generator;


@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TravelRepository travelRepository;
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private UserService userService;

	@Transactional
	public BaseMessage saveImage(List<FileDto> files, String userId, long travelIdx, long placeIdx) throws Exception {
		Map<String,Object> resultMap= new HashMap<>();
		
		// 파일리스트가 비어있는 경우
		if (files.isEmpty()) {
			return new BaseMessage(HttpStatus.NO_CONTENT);
		}

		// 실행되는 위치의 images 폴더에 파일이 저장됨
		String savePath = System.getProperty("user.dir") + "image";

		// 파일이 저장되는 폴더가 없으면 폴더 생성
		if (!new File(savePath).exists())
			new File(savePath).mkdir();

		// 다중 파일 업로드 처리
		for (FileDto file : files) {
			MultipartFile image = file.getFile();
			if (image == null) { // 파일이 빈 파일인 경우
				resultMap.put("errors", "image가 null");
				return new BaseMessage(HttpStatus.NO_CONTENT, resultMap);
			}

			String contentType = image.getContentType();

			if (contentType == null) { // 파일 확장자가 없는 경우
				resultMap.put("errors", "파일 확장자가 없음");
				return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
			}

			String imageOriginTitle = System.nanoTime() + image.getOriginalFilename(); // 파일명 중복을 피하기 위해 나노초까지 받아옴
			imageOriginTitle = imageOriginTitle.replace(" ", ""); // 파일이름 공백제거
			String imageTitle = new MD5Generator(imageOriginTitle).toString(); // 서버에 저장 값은 MD5의 체크섬 값

			String imagePath = savePath + "/" + imageOriginTitle;
			image.transferTo(new File(imagePath));

			Image im = Image.builder()
					.travel(travelRepository.findBytravelIdx(travelIdx).get())
					.place(placeRepository.findByplaceIdx(placeIdx).get())
					.imageOriginTitle(imageOriginTitle)
					.imageTitle(imageTitle)
					.imagePath(imagePath)
					.imageLat(file.getPosition().getLat())
					.imageLong(file.getPosition().getLng())
					.imageTime(file.getDateTime())
					.isPlaceLeader(file.getIsPlaceLeader())
					.isTravelLeader(file.getIsTravelLeader())
					.user(userRepository.findByUserId(userId).get())
					.imageWidth(file.getImageWidth())
					.imageHeight(file.getImageHeight())
					.build();

			im = imageRepository.save(im);
			resultMap.put("success", im.getImageIdx());
		}
		return new BaseMessage(HttpStatus.OK, resultMap);
	}


	@Transactional
	public BaseMessage saveProfile(MultipartFile file, String userId) throws IllegalStateException, IOException, NoSuchAlgorithmException {
		Map<String,Object> resultMap= new HashMap<>();

		// 실행되는 위치의 images 폴더에 파일이 저장됨
		String savePath = System.getProperty("user.dir") + "profile";

		// 파일이 저장되는 폴더가 없으면 폴더 생성
		if (!new File(savePath).exists())
			new File(savePath).mkdir();

		String contentType = file.getContentType();

		if (contentType == null) { // 파일 확장자가 없는 경우
			resultMap.put("errors", "파일 확장자가 없음");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}

		String imageOriginTitle = System.nanoTime() + file.getOriginalFilename(); // 파일명 중복을 피하기 위해 나노초까지 받아옴
		imageOriginTitle = imageOriginTitle.replace(" ", ""); // 파일이름 공백제거
		String imageTitle = new MD5Generator(imageOriginTitle).toString(); // 서버에 저장 값은 MD5의 체크섬 값

		String imagePath = savePath + "/" + imageOriginTitle;
		file.transferTo(new File(imagePath)	);

		Image im = Image.builder()
				.travel(null)
				.place(null)
				.imageOriginTitle(imageOriginTitle)
				.imageTitle(imageTitle)
				.imagePath(imagePath)
				.imageLat(0.0)
				.imageLong(0.0)
				.imageTime(null)
				.isPlaceLeader(0)
				.isTravelLeader(0)
				.user(userRepository.findByUserId(userId).get())
				.build();

		im = imageRepository.save(im);
		
		return new BaseMessage(HttpStatus.OK, im);
	}

	@Transactional
	public BaseMessage deleteImage(long imageIdx) {
		Map<String,Object> resultMap= new HashMap<>();
		Optional<Image> delImg = imageRepository.findByImageIdx(imageIdx);
		
		if (delImg.isPresent()) {
			imageRepository.delete(delImg.get());
			resultMap.put("success", "사진 삭제 성공");

			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "사진 삭제 실패 (존재하지 않은 사진)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}
}
