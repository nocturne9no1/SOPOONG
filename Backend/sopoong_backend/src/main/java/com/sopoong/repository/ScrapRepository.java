package com.sopoong.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sopoong.model.entity.Scrap;


public interface ScrapRepository extends JpaRepository<Scrap, Long> {
	Page<Scrap> findByUser_UserId(String userId, Pageable pageable);

	Optional<Scrap> findByUser_UserIdAndTravel_TravelIdx(String userId, Long travelIdx);
}
