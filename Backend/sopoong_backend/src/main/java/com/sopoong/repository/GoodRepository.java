package com.sopoong.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sopoong.model.entity.Good;

public interface GoodRepository extends JpaRepository<Good, Long>{
	Page<Good> findByTravel_TravelIdx(Long travelIdx,Pageable pageable);	
	Optional<Good> findByUser_UserIdAndTravel_TravelIdx(String userId,Long TravelIdx);
	
	long countByTravel_TravelIdx(long travelIdx);
}
