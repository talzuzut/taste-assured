package com.rating.service;

import com.rating.exception.RatingNotFoundException;
import com.rating.model.RestaurantRating;
import com.rating.model.RestaurantRatingDTO;
import com.rating.repository.RestaurantRatingRepository;
import com.rating.service.impl.RestaurantRatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class RestaurantRatingServiceTest {
	@Mock
	private RestaurantRatingRepository restaurantRatingRepository;
	@Mock
	private KafkaTemplate<String, Object> kafkaTemplate;
	@InjectMocks
	private RestaurantRatingServiceImpl restaurantRatingService;

	@Test
	public void givenRestaurantRatingDTO_whenAddRestaurantRating_thenRestaurantRatingAdded() {
		// given
		RestaurantRatingDTO restaurantRatingDTO = new RestaurantRatingDTO(
				1L,
				1L,
				5,
				"Great restaurant!"
		);
		ReflectionTestUtils.setField(restaurantRatingService, "ratingsRequiringFollowersData", "ratings-requiring-followers-data");
		when(restaurantRatingRepository.save(any())).thenReturn(new RestaurantRating());

		// when
		restaurantRatingService.addRestaurantRating(restaurantRatingDTO);
		// then
		verify(restaurantRatingRepository, times(1)).save(any());
		verify(kafkaTemplate, times(1)).send(anyString(), any());
		assertDoesNotThrow(() -> restaurantRatingService.addRestaurantRating(restaurantRatingDTO));
	}

	@Test
	public void givenRestaurantIdAndRecommenderId_whenDeleteRestaurantRating_thenRestaurantRatingDeleted() {
		// given
		Long restaurantId = 1L;
		Long recommenderId = 1L;
		when(restaurantRatingRepository.findByRestaurantIdAndRecommenderId(restaurantId, recommenderId)).thenReturn(Optional.of(new RestaurantRating()));
		// when
		restaurantRatingService.deleteRestaurantRating(restaurantId, recommenderId);
		// then
		verify(restaurantRatingRepository, times(1)).deleteByRestaurantIdAndRecommenderId(restaurantId, recommenderId);
	}

	@Test
	public void givenRestaurantNotExists_whenDeleteRestaurantRating_thenRatingNotFoundExceptionThrown() {
		// given
		Long restaurantId = 1L;
		Long recommenderId = 1L;
		when(restaurantRatingRepository.findByRestaurantIdAndRecommenderId(restaurantId, recommenderId)).thenReturn(Optional.empty());
		// when,then
		assertThrows(RatingNotFoundException.class, () -> restaurantRatingService.deleteRestaurantRating(restaurantId, recommenderId));
	}

	@Test
	public void givenRestaurantId_whenGetRestaurantRatingById_thenRestaurantRatingReturned_orNull() {
		// given
		String id = "1";
		when(restaurantRatingRepository.findById(id)).thenReturn(Optional.of(new RestaurantRating()));
		// when
		restaurantRatingService.getRestaurantRatingById(id);
		// then
		verify(restaurantRatingRepository, times(1)).findById(id);
		assertNotNull(restaurantRatingService.getRestaurantRatingById(id));
		assertNull(restaurantRatingService.getRestaurantRatingById("2"));
	}

	@Test
	public void givenRestaurantRatingDTO_whenUpdateRestaurantRating_thenRestaurantRatingUpdated() {
		// given
		RestaurantRatingDTO restaurantRatingDTO = new RestaurantRatingDTO(
				1L,
				1L,
				5,
				"Great restaurant!"
		);
		when(restaurantRatingRepository.findByRestaurantIdAndRecommenderId
				(restaurantRatingDTO.restaurantId(), restaurantRatingDTO.recommenderId()))
				.thenReturn(Optional.of(new RestaurantRating()));
		// when
		restaurantRatingService.updateRestaurantRating(restaurantRatingDTO);
		// then

		ArgumentCaptor<RestaurantRating> argumentCaptor = ArgumentCaptor.forClass(RestaurantRating.class);
		verify(restaurantRatingRepository, times(1)).save(argumentCaptor.capture());

		RestaurantRating capturedRating = argumentCaptor.getValue();
		assertEquals(restaurantRatingDTO.rating(), capturedRating.getRating());
		assertEquals(restaurantRatingDTO.comment(), capturedRating.getComment());
	}
}
