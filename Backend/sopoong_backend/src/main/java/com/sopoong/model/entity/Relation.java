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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.JoinColumnOrFormula;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long relationIdx;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumnOrFormula(column = @JoinColumn(name = "relation_following", referencedColumnName = "user_id")			
	)
	private User relationFollowing; // 팔로우 한 사람
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumnOrFormula(column = @JoinColumn(name = "relation_followed", referencedColumnName = "user_id")			
	)
	private User relationFollowed; // 팔로우 당한 사람
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "Relation [relationIdx=" + relationIdx + ", relationFollowing=" + relationFollowing.getUserId() + ", relationFollowed="
				+ relationFollowed.getUserId() + ", createdAt=" + createdAt + "]";
	}
	
	
	
	 
}
