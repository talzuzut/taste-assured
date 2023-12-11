package com.rating.service.api;

import com.rating.model.RestaurantRating;
import com.rating.model.RestaurantRatingDTO;

import java.util.List;

public interface RestaurantRatingService {
	RestaurantRating getRestaurantRating(Long restaurantId, Long recommenderId);

	void addRestaurantRating(RestaurantRatingDTO restaurantRatingDTO);

	void deleteRestaurantRating(Long restaurantId, Long recommenderId);

	boolean isRestaurantRatedByUser(Long restaurantId, Long recommenderId);

	List<RestaurantRating> getAllRatingsByUser(Long recommenderId);

	RestaurantRating getRestaurantRatingById(String id);

	void updateRestaurantRating(RestaurantRatingDTO restaurantRatingDTO);
}
