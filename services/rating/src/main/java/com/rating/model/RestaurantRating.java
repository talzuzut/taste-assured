package com.rating.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndexes({
		@CompoundIndex(def = "{'restaurantId': 1, 'recommenderId': 1}", unique = true)})
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class RestaurantRating {
	@Id
	private String id;
	private Long restaurantId;
	private Long recommenderId;
	private Integer rating;
	private String comment;


}