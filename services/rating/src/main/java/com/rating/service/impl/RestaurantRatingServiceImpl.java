package com.rating.service.impl;

import com.rating.exception.ErrorMessageProvider;
import com.rating.exception.RatingNotFoundException;
import com.rating.model.RatingDetails;
import com.rating.model.RestaurantRating;
import com.rating.model.RestaurantRatingDTO;
import com.rating.repository.RestaurantRatingRepository;
import com.rating.service.api.RestaurantRatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantRatingServiceImpl implements RestaurantRatingService {
	private final RestaurantRatingRepository restaurantRatingRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	@Value("${spring.kafka.producer.topic.ratings-requiring-followers-data}")
	private String ratingsRequiringFollowersData;

	public void addRestaurantRating(RestaurantRatingDTO restaurantRatingDTO) {
		RestaurantRating restaurantRating = convertDtoToEntity(restaurantRatingDTO);
		restaurantRatingRepository.save(restaurantRating);
		RatingDetails ratingDetails = new RatingDetails(restaurantRating.getId(), restaurantRating.getRecommenderId());
		publishRestaurantRatingToKafkaForFollowersEnrichment(ratingDetails);
	}


	private void publishRestaurantRatingToKafkaForFollowersEnrichment(RatingDetails ratingDetails) {
		kafkaTemplate.send(ratingsRequiringFollowersData, ratingDetails);
	}

	@Override
	public void deleteRestaurantRating(Long restaurantId, Long recommenderId) throws RatingNotFoundException {
		if (!isRestaurantRatedByUser(restaurantId, recommenderId)) {
			String errorMessage = ErrorMessageProvider.getErrorMessage("error.not_found");
			log.error(errorMessage);
			throw new RatingNotFoundException();
		}
		restaurantRatingRepository.deleteByRestaurantIdAndRecommenderId(restaurantId, recommenderId);
	}

	@Override
	public boolean isRestaurantRatedByUser(Long restaurantId, Long recommenderId) {
		return restaurantRatingRepository.findByRestaurantIdAndRecommenderId(restaurantId, recommenderId).isPresent();
	}

	@Override
	public List<RestaurantRating> getAllRatingsByUser(Long recommenderId) {
		return restaurantRatingRepository.findAllByRecommenderId(recommenderId);
	}

	@Override
	public RestaurantRating getRestaurantRatingById(String id) {
		return restaurantRatingRepository.findById(id).orElse(null);
	}

	@Override
	public void updateRestaurantRating(RestaurantRatingDTO restaurantRatingDTO) {
		restaurantRatingRepository.findByRestaurantIdAndRecommenderId(restaurantRatingDTO.restaurantId(), restaurantRatingDTO.recommenderId())
				.ifPresentOrElse(restaurantRating -> {
					if (restaurantRatingDTO.rating() != null) {
						restaurantRating.setRating(restaurantRatingDTO.rating());
					}
					if (restaurantRatingDTO.comment() != null) {
						restaurantRating.setComment(restaurantRatingDTO.comment());
					}
					restaurantRatingRepository.save(restaurantRating);
				}, () -> {
					throw new RatingNotFoundException();
				});
	}

	public RestaurantRating convertDtoToEntity(RestaurantRatingDTO restaurantRatingDTO) {
		return RestaurantRating.builder()
				.restaurantId(restaurantRatingDTO.restaurantId())
				.recommenderId(restaurantRatingDTO.recommenderId())
				.rating(restaurantRatingDTO.rating())
				.comment(restaurantRatingDTO.comment())
				.build();

	}


	@Override
	public RestaurantRating getRestaurantRating(Long restaurantId, Long recommenderId) {
		return restaurantRatingRepository.findByRestaurantIdAndRecommenderId(restaurantId, recommenderId).orElse(null);
	}
}
