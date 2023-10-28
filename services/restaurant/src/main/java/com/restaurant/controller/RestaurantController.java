package com.restaurant.controller;

import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.Restaurant;
import com.restaurant.service.impl.RestaurantServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/restaurant")
@AllArgsConstructor
public class RestaurantController {
	private final RestaurantServiceImpl restaurantServiceImpl;

	@GetMapping("/{id}")
	public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {

		log.info("Restaurant requested: {}", id);
		Restaurant restaurantById = restaurantServiceImpl.getRestaurantById(id);
		if (restaurantById == null) {
			throw new RestaurantNotFoundException();
		}
		return ResponseEntity.ok(restaurantById);

	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable String name) {

		log.info("Restaurant requested: {}", name);
		Restaurant restaurantByName = restaurantServiceImpl.getRestaurantByName(name);
		if (restaurantByName == null) {
			throw new RestaurantNotFoundException();
		}
		return ResponseEntity.ok(restaurantByName);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addRestaurantByName(@RequestBody String restaurantName) throws Exception {
		log.info("Adding Restaurant: {}", restaurantName);
		restaurantServiceImpl.addRestaurantByNameFromGoogleMaps(restaurantName);
	}


	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteRestaurantById(@PathVariable Long id) {
		log.info("Deleting Restaurant: {}", id);
		restaurantServiceImpl.deleteRestaurantById(id);
	}

	@DeleteMapping("/name/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteRestaurantByName(@PathVariable String name) {
		log.info("Deleting Restaurant: {}", name);
		restaurantServiceImpl.deleteRestaurantByName(name);

	}

}
