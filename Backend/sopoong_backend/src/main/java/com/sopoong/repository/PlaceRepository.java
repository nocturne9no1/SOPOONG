package com.sopoong.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopoong.model.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>{
	Optional<Place> findByplaceIdx(long placeIdx);
	List<Place> findByTravel_TravelIdx(long travelIdx);
}
