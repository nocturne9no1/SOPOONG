package com.sopoong.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sopoong.model.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long>{
	
	Page<Alarm> findByUser_UserId(String userId,Pageable pageable);
	
}
