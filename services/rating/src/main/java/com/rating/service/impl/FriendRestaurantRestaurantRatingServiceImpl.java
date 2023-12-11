package com.rating.service.impl;

import com.mongodb.MongoException;
import com.rating.exception.EmptyKafkaMessageException;
import com.rating.exception.RatingNotFoundException;
import com.rating.model.FriendRestaurantRating;
import com.rating.model.FriendRestaurantRatingDTO;
import com.rating.model.RatingWithFollowersList;
import com.rating.model.RestaurantRating;
import com.rating.repository.FriendRestaurantRatingRepository;
import com.rating.service.api.FriendRestaurantRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRestaurantRestaurantRatingServiceImpl implements FriendRestaurantRatingService {
	private final FriendRestaurantRatingRepository friendRestaurantRatingRepository;
	private final RestaurantRatingServiceImpl restaurantRatingService;

	@Override
	public void addFriendRestaurantRating(FriendRestaurantRating friendRestaurantRating) throws DuplicateKeyException, MongoException {
		friendRestaurantRatingRepository.save(friendRestaurantRating);
	}

	@Override
	public void addBatchFriendRestaurantRating(List<FriendRestaurantRating> friendRestaurantRatings) {
		try {
			friendRestaurantRatingRepository.saveAll(friendRestaurantRatings);
		} catch (DuplicateKeyException | MongoException e) {
			log.error("Error saving friend restaurant ratings: {}", e.getMessage());
		}
	}

	@Override
	public void deleteFriendRestaurantRating(Long restaurantId, Long recommenderId, Long targetId) {
		if (!isFriendRestaurantRatingExists(restaurantId, recommenderId, targetId)) {
			throw new RatingNotFoundException();
		}
		friendRestaurantRatingRepository.deleteByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId);
	}

	@Override
	public FriendRestaurantRating getFriendRestaurantRating(Long restaurantId, Long recommenderId, Long targetId) {
		return friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId).orElse(null);
	}


	@Override
	public void updateFriendRating(FriendRestaurantRatingDTO friendRestaurantRatingDTO) {
		friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(friendRestaurantRatingDTO.restaurantId(), friendRestaurantRatingDTO.recommenderId(), friendRestaurantRatingDTO.targetId())
				.ifPresentOrElse(friendRestaurantRating -> {
					if (friendRestaurantRatingDTO.rating() != null) {
						friendRestaurantRating.setRating(friendRestaurantRatingDTO.rating());
					}
					if (friendRestaurantRatingDTO.comment() != null) {
						friendRestaurantRating.setComment(friendRestaurantRatingDTO.comment());
					}
					friendRestaurantRatingRepository.save(friendRestaurantRating);
				}, () -> {
					throw new RatingNotFoundException();
				});

	}

	@Override
	public boolean isFriendRestaurantRatingExists(Long restaurantId, Long recommenderId, Long targetId) {

		return friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId).isPresent();
	}

	/*	@Override
		@KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
		public void updateFollowersWithRating(RestaurantRatingDTO restaurantRatingDTO) {
			log.info("Kafka topic: Received restaurant rating: {}", restaurantRatingDTO);
		}*/
	@Override
	@KafkaListener(topics = "${spring.kafka.consumer.topic.ratings-with-followers-data}", groupId = "${spring.kafka.consumer.group-id}")
	public void updateFollowersWithRating(RatingWithFollowersList ratingWithFollowersList) {
		RestaurantRating restaurantRating = restaurantRatingService.getRestaurantRatingById(ratingWithFollowersList.ratingId());
		if (restaurantRating != null) {
			List<FriendRestaurantRating> friendRestaurantRatings = ratingWithFollowersList.followers().stream().parallel().map(
					followerId -> convertToFriendRestaurantRating(restaurantRating, followerId)
			).toList();
			addBatchFriendRestaurantRating(friendRestaurantRatings);
			log.info("Kafka topic: Received restaurant rating: {}", ratingWithFollowersList);
		} else {
			log.error("Kafka topic: Restaurant rating not found: {}", ratingWithFollowersList);
			throw new EmptyKafkaMessageException("Empty kafka message when updating followers with rating");
		}
	}

	private FriendRestaurantRating convertToFriendRestaurantRating(RestaurantRating restaurantRating, Long followerId) {
		return FriendRestaurantRating.builder()
				.restaurantId(restaurantRating.getRestaurantId())
				.targetId(followerId)
				.recommenderId(restaurantRating.getRecommenderId())
				.rating(restaurantRating.getRating())
				.comment(restaurantRating.getComment())
				.build();
	}

	@Override
	public FriendRestaurantRating convertDtoToEntity(FriendRestaurantRatingDTO friendRestaurantRatingDTO) {
		return FriendRestaurantRating.builder()
				.restaurantId(friendRestaurantRatingDTO.restaurantId())
				.recommenderId(friendRestaurantRatingDTO.recommenderId())
				.targetId(friendRestaurantRatingDTO.targetId())
				.rating(friendRestaurantRatingDTO.rating())
				.comment(friendRestaurantRatingDTO.comment())
				.build();
	}

}
