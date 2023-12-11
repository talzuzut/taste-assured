package com.user_management.service.api;

import com.user_management.model.RatingDetails;
import org.springframework.kafka.annotation.KafkaListener;

public interface RatingEnrichmentService {
	@KafkaListener(topics = "${spring.kafka.consumer.topic.ratings-requiring-followers-data}", groupId = "${spring.kafka.consumer.group-id}")
	void enrichRatingsWithFollowersData(RatingDetails ratingDetails);
}
