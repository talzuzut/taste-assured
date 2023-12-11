package com.rating.service;

import com.rating.exception.EmptyKafkaMessageException;
import com.rating.exception.RatingNotFoundException;
import com.rating.model.FriendRestaurantRating;
import com.rating.model.FriendRestaurantRatingDTO;
import com.rating.model.RatingWithFollowersList;
import com.rating.model.RestaurantRating;
import com.rating.repository.FriendRestaurantRatingRepository;
import com.rating.service.impl.FriendRestaurantRestaurantRatingServiceImpl;
import com.rating.service.impl.RestaurantRatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendRestaurantRestaurantRatingServiceTest {

	@Mock
	private FriendRestaurantRatingRepository friendRestaurantRatingRepository;

	@Mock
	private RestaurantRatingServiceImpl restaurantRatingService;

	@InjectMocks
	private FriendRestaurantRestaurantRatingServiceImpl friendRestaurantRestaurantRatingService;

	@Test
	public void givenFriendRestaurantRating_whenAddFriendRestaurantRating_thenFriendRestaurantRatingAdded() {
		// given
		FriendRestaurantRating friendRestaurantRating = new FriendRestaurantRating();
		// when
		friendRestaurantRestaurantRatingService.addFriendRestaurantRating(friendRestaurantRating);
		// then
		verify(friendRestaurantRatingRepository, times(1)).save(friendRestaurantRating);

	}

	@Test
	public void givenListOfFriendRestaurantRating_whenAddBatchFriendRestaurantRating_thenFriendRestaurantRatingsAdded() {
		// given
		FriendRestaurantRating friendRestaurantRating = new FriendRestaurantRating();
		// when
		friendRestaurantRestaurantRatingService.addBatchFriendRestaurantRating(List.of(friendRestaurantRating));
		// then
		verify(friendRestaurantRatingRepository, times(1)).saveAll(List.of(friendRestaurantRating));

	}

	@Test
	public void givenFriendRestaurantRating_whenDeleteFriendRestaurantRating_thenFriendRestaurantRatingDeleted() {
		// given
		Long restaurantId = 1L;
		Long recommenderId = 1L;
		Long targetId = 1L;
		when(friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId)).thenReturn(Optional.of(new FriendRestaurantRating()));
		// when
		friendRestaurantRestaurantRatingService.deleteFriendRestaurantRating(restaurantId, recommenderId, targetId);
		// then
		verify(friendRestaurantRatingRepository, times(1)).deleteByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId);
	}

	@Test
	public void givenNonExistingFriendRestaurantRating_whenDeleteFriendRestaurantRating_thenRatingNotFoundExceptionThrown() {
		// given
		Long restaurantId = 1L;
		Long recommenderId = 1L;
		Long targetId = 1L;
		when(friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(restaurantId, recommenderId, targetId)).thenReturn(Optional.empty());
		// when,then
		assertThrows(RatingNotFoundException.class, () -> friendRestaurantRestaurantRatingService.deleteFriendRestaurantRating(restaurantId, recommenderId, targetId));
	}

	@Test
	public void givenFriendRestaurantRating_whenUpdateFriendRating_thenFriendRestaurantRatingUpdated() {
		// given
		FriendRestaurantRatingDTO friendRestaurantRatingDTO = new FriendRestaurantRatingDTO(
				1L,
				1L,
				1L,
				5,
				"Great restaurant!"
		);
		when(friendRestaurantRatingRepository.findByRestaurantIdAndRecommenderIdAndTargetId(friendRestaurantRatingDTO.restaurantId(), friendRestaurantRatingDTO.recommenderId(), friendRestaurantRatingDTO.targetId()))
				.thenReturn(Optional.of(new FriendRestaurantRating()));
		// when
		friendRestaurantRestaurantRatingService.updateFriendRating(friendRestaurantRatingDTO);
		// then
		ArgumentCaptor<FriendRestaurantRating> argumentCaptor = ArgumentCaptor.forClass(FriendRestaurantRating.class);
		verify(friendRestaurantRatingRepository, times(1)).save(argumentCaptor.capture());

		FriendRestaurantRating capturedRating = argumentCaptor.getValue();
		assertEquals(friendRestaurantRatingDTO.rating(), capturedRating.getRating());
		assertEquals(friendRestaurantRatingDTO.comment(), capturedRating.getComment());
	}

	@Test
	public void givenRatingWithFollowersList_whenUpdateFollowersWithRating_thenFriendRestaurantRatingsAdded() {
		// given
		RatingWithFollowersList ratingWithFollowersList = new RatingWithFollowersList("1", List.of(1L, 2L, 3L));
		RestaurantRating restaurantRating = new RestaurantRating();
		when(restaurantRatingService.getRestaurantRatingById(ratingWithFollowersList.ratingId())).thenReturn(restaurantRating);
		// when
		friendRestaurantRestaurantRatingService.updateFollowersWithRating(ratingWithFollowersList);
		// then
		verify(friendRestaurantRatingRepository, times(1)).saveAll(anyList());
	}

	@Test
	public void givenRatingWithFollowersList_whenUpdateFollowersWithRatingAndRatingNotFound_thenEmptyKafkaMessageExceptionThrown() {
		// given
		RatingWithFollowersList ratingWithFollowersList = new RatingWithFollowersList("1", List.of(1L, 2L, 3L));
		when(restaurantRatingService.getRestaurantRatingById(ratingWithFollowersList.ratingId())).thenReturn(null);
		// when,then
		assertThrows(EmptyKafkaMessageException.class, () -> friendRestaurantRestaurantRatingService.updateFollowersWithRating(ratingWithFollowersList));
	}
}