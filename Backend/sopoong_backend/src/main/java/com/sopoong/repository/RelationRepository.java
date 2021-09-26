package com.sopoong.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopoong.model.entity.Relation;
import com.sopoong.model.entity.User;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long>{
	
	Optional<Relation> findByRelationFollowingAndRelationFollowed(User relationFollowing, User relationFollowed);
	
	Page<Relation> findByRelationFollowing(Pageable pageable, User user);
	Page<Relation> findByRelationFollowed(Pageable pageable, User user);
	
}
