package com.user_management.service;

import com.user_management.model.RatingDetails;
import com.user_management.model.RatingWithFollowersList;
import com.user_management.repository.UserRepository;
import com.user_management.service.impl.FollowRelationshipServiceImpl;
import com.user_management.service.impl.RatingEnrichmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class RatingEnrichmentServiceTest {
	@Mock
	private FollowRelationshipServiceImpl followRelationshipService;
	@Mock
	private KafkaTemplate<String, Object> kafkaTemplate;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private RatingEnrichmentServiceImpl ratingEnrichmentService;

	@Test
	public void givenRatingDetails_whenEnrichRatingsWithFollowersData_thenSendToKafka() {
		//given
		String ratingId = "1";
		Long raterId = 2L;
		RatingDetails ratingDetails = new RatingDetails(ratingId, raterId);
		List<Long> firstIdsList = List.of(1L, 3L);
		Page<Long> firstPage = mock(Page.class);
		when(firstPage.hasNext()).thenReturn(true);
		when(firstPage.getContent()).thenReturn(firstIdsList);

		List<Long> secondIdsList = List.of(4L, 5L);
		Page<Long> secondPage = mock(Page.class);
		when(secondPage.getContent()).thenReturn(secondIdsList);
		when(secondPage.hasNext()).thenReturn(false);
		when(followRelationshipService.getFollowersByUserId(raterId, PageRequest.of(0, 100))).thenReturn(firstPage);
		when(followRelationshipService.getFollowersByUserId(raterId, PageRequest.of(1, 100))).thenReturn(secondPage);

		String kafkaTopicName = "ratings-with-followers-data";
		ReflectionTestUtils.setField(ratingEnrichmentService, "ratingsWithFollowersDataTopic", kafkaTopicName);
		//when
		ratingEnrichmentService.enrichRatingsWithFollowersData(ratingDetails);
		//then
		ArgumentCaptor<RatingWithFollowersList> argumentCaptor = ArgumentCaptor.forClass(RatingWithFollowersList.class);
		verify(kafkaTemplate, times(2)).send(eq(kafkaTopicName), argumentCaptor.capture());
		List<RatingWithFollowersList> allValues = argumentCaptor.getAllValues();
		assertThat(allValues).extracting(RatingWithFollowersList::ratingId).containsExactly(ratingId, ratingId);
		assertThat(allValues).extracting(RatingWithFollowersList::followers).containsExactly(firstIdsList, secondIdsList);
	}

}
