package com.restaurant.service.api;

import com.google.maps.model.PlaceDetails;
import com.restaurant.exception.RestaurantExistsException;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.Restaurant;

/**
 * This interface defines the contract for a service that manages restaurant-related operations.
 */
public interface RestaurantService {

	/**
	 * Adds a new restaurant to the system.
	 *
	 * @param restaurant The restaurant to be added.
	 * @throws RestaurantExistsException If a restaurant with the same name already exists.
	 */
	void addRestaurant(Restaurant restaurant) throws RestaurantExistsException;

	/**
	 * Adds a restaurant by name from Google Maps to the system.
	 *
	 * @param restaurantName The name of the restaurant to fetch from Google Maps and add.
	 * @throws Exception If there is an issue fetching the restaurant data.
	 */
	void addRestaurantByNameFromGoogleMaps(String restaurantName) throws Exception;

	/**
	 * Retrieves a restaurant by its unique identifier.
	 *
	 * @param id The unique identifier of the restaurant.
	 * @return The restaurant if found, or null if not found.
	 */
	Restaurant getRestaurantById(Long id);

	/**
	 * Deletes a restaurant by its unique identifier.
	 *
	 * @param id The unique identifier of the restaurant to delete.
	 * @throws RestaurantNotFoundException If the restaurant does not exist.
	 */
	void deleteRestaurantById(Long id) throws RestaurantNotFoundException;

	/**
	 * Retrieves restaurant details from Google Maps by name.
	 *
	 * @param restaurantName The name of the restaurant to fetch from Google Maps.
	 * @return The restaurant details.
	 * @throws Exception If there is an issue fetching the restaurant data.
	 */
	Restaurant fetchRestaurantByNameFromGoogleMaps(String restaurantName) throws Exception;

	/**
	 * Converts Google Maps PlaceDetails to a Restaurant.
	 *
	 * @param placeDetails The PlaceDetails to convert.
	 * @return The converted Restaurant.
	 */
	Restaurant convertPlaceDetailsToRestaurant(PlaceDetails placeDetails);

	/**
	 * Checks if a restaurant with the given unique identifier exists.
	 *
	 * @param id The unique identifier of the restaurant to check.
	 * @return true if the restaurant exists, false otherwise.
	 */

	boolean restaurantExistsById(Long id);

	/**
	 * Checks if a restaurant with the given name exists.
	 *
	 * @param name The name of the restaurant to check.
	 * @return true if the restaurant exists, false otherwise.
	 */
	boolean restaurantExistsByName(String name);

	/**
	 * Deletes a restaurant by its name.
	 *
	 * @param name The name of the restaurant to delete.
	 * @throws RestaurantNotFoundException If the restaurant does not exist.
	 */
	void deleteRestaurantByName(String name) throws RestaurantNotFoundException;
}
