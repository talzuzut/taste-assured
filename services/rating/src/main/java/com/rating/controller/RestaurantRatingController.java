package com.rating.controller;

import com.mongodb.MongoException;
import com.rating.exception.RatingNotFoundException;
import com.rating.model.RestaurantRating;
import com.rating.model.RestaurantRatingDTO;
import com.rating.service.impl.RestaurantRatingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rating/restaurant")
@RequiredArgsConstructor
public class RestaurantRatingController {
	private final RestaurantRatingServiceImpl ratingService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void addRestaurantRating(@RequestBody RestaurantRatingDTO restaurantRatingDTO) throws DuplicateKeyException, MongoException {
		ratingService.addRestaurantRating(restaurantRatingDTO);

	}

	@GetMapping()
	public ResponseEntity<RestaurantRating> getRestaurantRating(@RequestParam Long restaurantId, @RequestParam Long recommenderId) throws RatingNotFoundException {
		RestaurantRating restaurantRating = ratingService.getRestaurantRating(restaurantId, recommenderId);
		if (restaurantRating == null) {
			throw new RatingNotFoundException();
		} else {
			return ResponseEntity.ok(restaurantRating);
		}
	}

	@DeleteMapping()
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteRestaurantRating(@RequestParam Long restaurantId, @RequestParam Long recommenderId) throws RatingNotFoundException {
		ratingService.deleteRestaurantRating(recommenderId, restaurantId);
	}

	@GetMapping("/recommender/{recommenderId}")
	public ResponseEntity<List<RestaurantRating>> getAllRatingsByUser(@PathVariable Long recommenderId) {
		List<RestaurantRating> ratingList = ratingService.getAllRatingsByUser(recommenderId);
		if (ratingList.isEmpty()) {
			throw new RatingNotFoundException("No ratings found for this user");
		} else {
			return ResponseEntity.ok(ratingList);
		}
	}

	@PutMapping
	@ResponseStatus(code = HttpStatus.OK)
	public void updateRestaurantRating(@RequestBody RestaurantRatingDTO restaurantRatingDTO) {
		ratingService.updateRestaurantRating(restaurantRatingDTO);
	}

}