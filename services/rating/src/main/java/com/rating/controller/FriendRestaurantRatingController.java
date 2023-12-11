package com.rating.controller;

import com.mongodb.MongoException;
import com.rating.model.FriendRestaurantRating;
import com.rating.model.FriendRestaurantRatingDTO;
import com.rating.service.api.FriendRestaurantRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO - DELETE THIS FILE, data should be injected from kafka stream
//
@RestController
@RequestMapping("/api/v1/friend_rating/restaurant")
@RequiredArgsConstructor
public class FriendRestaurantRatingController {
	private final FriendRestaurantRatingService friendRestaurantRatingService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addRestaurantRating(@RequestBody FriendRestaurantRatingDTO friendRestaurantRatingDTO) throws DuplicateKeyException, MongoException {
		FriendRestaurantRating friendRestaurantRating = friendRestaurantRatingService.convertDtoToEntity(friendRestaurantRatingDTO);
		friendRestaurantRatingService.addFriendRestaurantRating(friendRestaurantRating);
	}

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<FriendRestaurantRating> getRestaurantRating(@RequestParam Long restaurantId, @RequestParam Long recommenderId, @RequestParam Long targetId) {
		FriendRestaurantRating friendRestaurantRating = friendRestaurantRatingService.getFriendRestaurantRating(restaurantId, recommenderId, targetId);
		return friendRestaurantRating == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(friendRestaurantRating);
	}

	@DeleteMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void deleteRestaurantRating(@RequestParam Long restaurantId, @RequestParam Long recommenderId, @RequestParam Long targetId) {
		friendRestaurantRatingService.deleteFriendRestaurantRating(restaurantId, recommenderId, targetId);
	}

	@PutMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void updateRestaurantRating(@RequestBody FriendRestaurantRatingDTO friendRestaurantRatingDTO) {
		friendRestaurantRatingService.updateFriendRating(friendRestaurantRatingDTO);
	}


}
