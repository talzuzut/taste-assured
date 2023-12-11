package com.rating.service.api;

import com.rating.model.FriendRestaurantRating;
import com.rating.model.FriendRestaurantRatingDTO;
import com.rating.model.RatingWithFollowersList;

import java.util.List;

public interface FriendRestaurantRatingService {
	void addFriendRestaurantRating(FriendRestaurantRating friendRestaurantRating);

	void addBatchFriendRestaurantRating(List<FriendRestaurantRating> friendRestaurantRatings);

	void deleteFriendRestaurantRating(Long restaurantId, Long recommenderId, Long targetId);

	FriendRestaurantRating getFriendRestaurantRating(Long restaurantId, Long recommenderId, Long targetId);

	void updateFriendRating(FriendRestaurantRatingDTO friendRestaurantRatingDTO);

	boolean isFriendRestaurantRatingExists(Long restaurantId, Long recommenderId, Long targetId);

	public void updateFollowersWithRating(RatingWithFollowersList ratingWithFollowersList);

	FriendRestaurantRating convertDtoToEntity(FriendRestaurantRatingDTO friendRestaurantRatingDTO);
}
