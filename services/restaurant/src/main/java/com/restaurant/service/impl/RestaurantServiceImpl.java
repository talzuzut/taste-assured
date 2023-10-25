package com.restaurant.service.impl;

import com.google.maps.model.PlaceDetails;
import com.restaurant.exception.RestaurantExistsException;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.Restaurant;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.service.api.RestaurantService;
import com.restaurant.utils.GoogleMapsService;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final GoogleMapsService googleMapsService;

	// CRUD Operations

	public void addRestaurant(@NonNull Restaurant restaurant) {
		if (restaurantExistsByName(restaurant.getName())) {
			throw new RestaurantExistsException();
		}
		restaurantRepository.save(restaurant);
	}

	public void addRestaurantByNameFromGoogleMaps(@NotBlank String restaurantName) throws Exception {
		Restaurant restaurant = fetchRestaurantByNameFromGoogleMaps(restaurantName);
		addRestaurant(restaurant);
	}

	public Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id).orElse(null);
	}

	public void deleteRestaurantById(Long id) {
		log.info("Deleting Restaurant: {}", id);
		if (!restaurantExistsById(id)) {
			throw new RestaurantNotFoundException();
		}
		restaurantRepository.deleteById(id);
	}

	public void deleteRestaurantByName(String name) {
		if (!restaurantExistsByName(name)) {
			throw new RestaurantNotFoundException();
		}
		restaurantRepository.deleteByName(name);
	}

	// Data Retrieval and Validation

	public Restaurant fetchRestaurantByNameFromGoogleMaps(@NotBlank String restaurantName) throws Exception {
		CompletableFuture<PlaceDetails> restaurantFuture = googleMapsService.getPlaceDetailsByQueryAsync(restaurantName);
		PlaceDetails restaurantDetails = restaurantFuture.join();
		return convertPlaceDetailsToRestaurant(restaurantDetails);
	}

	public Restaurant convertPlaceDetailsToRestaurant(PlaceDetails placeDetails) {
		return Restaurant.builder()
				.name(placeDetails.name)
				.address(placeDetails.formattedAddress)
				.phone(placeDetails.formattedPhoneNumber)
				.longitude(BigDecimal.valueOf(placeDetails.geometry.location.lng))
				.latitude(BigDecimal.valueOf(placeDetails.geometry.location.lat))
				.googleMapsUrl(placeDetails.url)
				.websiteUrl(placeDetails.website)
				.openingHours(placeDetails.openingHours)
				.priceLevel(placeDetails.priceLevel)
				.wheelchairAccessibleEntrance(placeDetails.wheelchairAccessibleEntrance)
				.createdAt(LocalDateTime.now())
				.build();
	}

	// Existence Checks

	public boolean restaurantExistsById(Long id) {
		return restaurantRepository.existsById(id);
	}

	public boolean restaurantExistsByName(String name) {
		return restaurantRepository.existsByName(name);
	}
}
