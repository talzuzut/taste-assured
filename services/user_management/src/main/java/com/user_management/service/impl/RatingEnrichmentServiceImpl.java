package com.user_management.service.impl;

import com.user_management.model.RatingDetails;
import com.user_management.model.RatingWithFollowersList;
import com.user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties
public class RatingEnrichmentServiceImpl implements com.user_management.service.api.RatingEnrichmentService {

	@Value("${spring.kafka.producer.topic.ratings-with-followers-data}")
	private String ratingsWithFollowersDataTopic;
	private final FollowRelationshipServiceImpl followRelationshipService;

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final UserRepository userRepository;

	@Override
	@KafkaListener(topics = "${spring.kafka.consumer.topic.ratings-requiring-followers-data}", groupId = "${spring.kafka.consumer.group-id}")
	public void enrichRatingsWithFollowersData(RatingDetails ratingDetails) {
		int pageNumber = 0;
		Page<Long> followersIdsPage;

		do {
			followersIdsPage = followRelationshipService.getFollowersByUserId(ratingDetails.raterId(), PageRequest.of(pageNumber++, 100));
			if (followersIdsPage.isEmpty()) {
				break;
			}
			List<Long> followersIds = followersIdsPage.getContent();

			RatingWithFollowersList message = new RatingWithFollowersList(ratingDetails.ratingId(), followersIds);
			kafkaTemplate.send(ratingsWithFollowersDataTopic, message);
		} while (followersIdsPage.hasNext());
	}


}
