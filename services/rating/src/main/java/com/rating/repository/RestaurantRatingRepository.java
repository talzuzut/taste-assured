package com.rating.repository;

import com.rating.model.RestaurantRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRatingRepository extends MongoRepository<RestaurantRating, Long> {
	Optional<RestaurantRating> findById(String id);

	List<RestaurantRating> findAllByRestaurantId(Long restaurantId);

	List<RestaurantRating> findAllByRecommenderId(Long recommenderId);

	Optional<RestaurantRating> findByRestaurantIdAndRecommenderId(Long restaurantId, Long recommenderId);

	void deleteByRestaurantIdAndRecommenderId(Long restaurantId, Long recommenderId);
}
