package com.sopoong.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "image")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long imageIdx;
	
	@NotNull
	@Column(name = "image_origin_title")
	private String imageOriginTitle; // 업로드 된 실제 파일명
	
	@NotNull
	private String imageTitle; // 서버에 저장된 파일명
	
	@NotNull
	private String imagePath; // 파일 저장 경로
	
	@NotNull
	private double imageLat; // 파일 위도
	
	@NotNull
	private double imageLong; // 파일 경도
	
	private int imageWidth;
	
	private int imageHeight;
	
	private LocalDateTime imageTime; // 사진 찍은 시각
	
	@NotNull
	private int isPlaceLeader; // 위치 대표사진 여부(0:일반 사진, 1:위치대표이미지)
	
	@NotNull
	private int isTravelLeader; // 여행 대표사진 여부(0:일반 사진, 1:여행대표이미지)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "travel_idx")
	private Travel travel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_idx")
	private Place place;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "Image [imageIdx=" + imageIdx + ", imageOriginTitle=" + imageOriginTitle + ", imageTitle=" + imageTitle
				+ ", imagePath=" + imagePath + ", imageLat=" + imageLat + ", imageLong=" + imageLong + ", travel="
				+ travel + ", place=" + place + ", user=" + user + ", createdAt=" + createdAt + "]";
	}
}
