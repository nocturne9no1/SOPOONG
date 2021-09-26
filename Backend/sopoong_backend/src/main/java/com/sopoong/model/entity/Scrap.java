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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
public class Scrap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long scrapIdx;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumnOrFormula(column = @JoinColumn(name = "user_id", referencedColumnName = "user_id")			
	)
	private User user;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "travel_idx")
	private Travel travel;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return "Scrap [scrapIdx=" + scrapIdx + ", user=" + user.getUserId() + ", travel=" + travel.getTravelIdx() + ", createdAt=" + createdAt
				+ "]";
	}
	
	
}
