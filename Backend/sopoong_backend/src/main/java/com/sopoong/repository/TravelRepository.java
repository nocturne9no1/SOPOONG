package com.sopoong.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopoong.model.entity.Travel;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
	Page<Travel> findByUser_UserIdInAndTravelIsVisible(List<String> userIds, int travelIsVisible, Pageable pageable);
	List<Travel> findByUser_UserId(String userId);
	Optional<Travel> findBytravelIdx(long travelIdx);
}
