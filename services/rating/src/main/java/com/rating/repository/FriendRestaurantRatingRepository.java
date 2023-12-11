package com.rating.repository;

import com.rating.model.FriendRestaurantRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRestaurantRatingRepository extends MongoRepository<FriendRestaurantRating, String> {
	void deleteByRestaurantIdAndRecommenderIdAndTargetId(Long restaurantId, Long recommenderId, Long targetId);

	Optional<FriendRestaurantRating> findByRestaurantIdAndRecommenderIdAndTargetId(Long restaurantId, Long recommenderId, Long targetId);
}
