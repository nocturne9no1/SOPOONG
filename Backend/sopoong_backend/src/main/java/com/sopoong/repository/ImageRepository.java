package com.sopoong.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopoong.model.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
	Optional<Image> findByImageIdx(long imageIdx);
	Optional<Image> findImageByUser_UserId(String userId);
	Optional<Image> findImageByTravel_TravelIdxAndIsTravelLeader(long travelIdx, int isTravelLeader);
	Optional<Image> findImageByPlace_PlaceIdxAndIsPlaceLeader(long placeIdx, int isPlaceLeader);
	List<Image> findByTravel_TravelIdx(long travelIdx);
	List<Image> findByPlace_PlaceIdx(long placeIdx);
	
}
