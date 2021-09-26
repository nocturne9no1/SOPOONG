package com.sopoong.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
public class Travel implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private long travelIdx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	
	@NotNull
	private String travelTitle;
	
	@OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
	private List<Place> placeIdxList = new ArrayList<Place>();
	
	private String travelContent;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_idx")
	private Image image;
	
	@Column(name="travel_is_visible", columnDefinition = "TINYINT", length=1)
	private int travelIsVisible;
	
	@Column(name="is_follow", columnDefinition = "TINYINT", length=1)
	private int isFollow;
	
	private double travelLat;
	
	private double travelLong;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endDate;
	
	@Column(insertable = false, updatable = true)
	private LocalDateTime updatedAt;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;
	
//	@OneToMany
//	@JoinColumn(name = "travel_idx")
//	private List<Place> places = new ArrayList<>();
	
	@OneToMany(mappedBy = "travel")
	private List<Good> goods = new ArrayList<>();

	@Override
	public String toString() {
		String s = "Travel [travelIdx=" + travelIdx + ", user=" + user.getUserId() + ", travelTitle=" + travelTitle 
		+ ", travelContent=" + travelContent + ", image=" + image.getImageIdx() + ", travelIsVisible=" + travelIsVisible
		+ ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + ", places=";
		
		s+=", likes=";
		for(int i=0;i<goods.size();i++) {
			s+=goods.get(i).toString();
		}
		s+="]";
		
		return s;
	}	
}
