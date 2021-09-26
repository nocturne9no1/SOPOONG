package com.sopoong.model.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
	private MultipartFile file; // 사진 파일
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime dateTime; // 사진 찍은 시각
	private ImagePosition position; // 사진 위도 & 경도
	private int isPlaceLeader; // 위치대표사진인지
	private int isTravelLeader; // 여행대표사진인지
	private int imageWidth;
	private int imageHeight;
}
