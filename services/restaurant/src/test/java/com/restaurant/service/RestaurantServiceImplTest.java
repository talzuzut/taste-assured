package com.restaurant.service;

import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.restaurant.exception.RestaurantNotFoundException;
import com.restaurant.model.Restaurant;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.service.impl.RestaurantServiceImpl;
import com.restaurant.utils.GoogleMapsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {
	private final String validRestaurantName = "Valid Restaurant Name";
	private final String validRestaurantAddress = "Valid Restaurant Address";
	private final String validPhoneNumber = "+972 52 123 4567";
	private final Point validLocation = new Point(32.123456, 34.123456);
	private final URL validGoogleMapsUrl = new URL("https://maps.google.com/?cid=123456789");
	private final URL validWebsiteUrl = new URL("https://google.com");
	private final PriceLevel priceLevel = PriceLevel.MODERATE;
	private final OpeningHours openingHours = new OpeningHours();

	@Mock
	private RestaurantRepository restaurantRepository;


	@Mock
	private GoogleMapsService googleMapsService;
	@InjectMocks
	private RestaurantServiceImpl restaurantServiceImpl;

	public RestaurantServiceImplTest() throws MalformedURLException {
	}


	@Test
	public void givenValidRestaurant_whenAddingRestaurant_thenRestaurantIsSaved() {
		// Given
		Restaurant restaurantToSave = createValidRestaurant();

		// When
		restaurantServiceImpl.addRestaurant(restaurantToSave);

		// Then
		verify(restaurantRepository, times(1)).save(restaurantToSave);
	}

	@Test
	public void givenExistingRestaurantId_whenCheckingIfExists_thenReturnsTrue() {
		// Given
		Long existingId = 1L;

		when(restaurantRepository.existsById(existingId)).thenReturn(true);

		// When
		boolean exists = restaurantServiceImpl.restaurantExistsById(existingId);

		// Then
		assertTrue(exists);
	}

	@Test
	public void givenNonExistingRestaurantId_whenCheckingIfExists_thenReturnsFalse() {
		// Given
		Long nonExistingId = 2L;

		when(restaurantRepository.existsById(nonExistingId)).thenReturn(false);

		// When
		boolean exists = restaurantServiceImpl.restaurantExistsById(nonExistingId);

		// Then
		assertFalse(exists);
	}

	@Test
	public void givenValidRestaurantName_whenFetchingFromGoogleMaps_thenRestaurantIsFetched() throws Exception {
		// Given
		String restaurantName = "Valid Restaurant Name";
		Restaurant validRestaurant = createValidRestaurant();
		when(googleMapsService.getPlaceDetailsByQueryAsync(restaurantName))
				.thenReturn(CompletableFuture.completedFuture(createValidPlaceDetails()));

		// When
		Restaurant fetchedRestaurant = restaurantServiceImpl.fetchRestaurantByNameFromGoogleMaps(restaurantName);

		// Then

//		assertTrue(new ReflectionEquals(validRestaurant).matches(fetchedRestaurant));
		assertThat(fetchedRestaurant).usingRecursiveComparison().ignoringFields("id", "createdAt").isEqualTo(validRestaurant);
		// Add more assertions as needed for the expected properties of the fetched restaurant.
	}

	@Test
	public void givenInvalidRestaurantName_whenFetchingFromGoogleMaps_thenExceptionIsThrown() throws IOException, InterruptedException, ApiException {
		// Given
		String invalidRestaurantName = "*&*&*&";

		when(googleMapsService.getPlaceDetailsByQueryAsync(invalidRestaurantName))
				.thenReturn(CompletableFuture.failedFuture(new Exception("Google Maps API Error")));

		// When & Then
		assertThrows(Exception.class, () -> restaurantServiceImpl.fetchRestaurantByNameFromGoogleMaps(invalidRestaurantName));
	}

	@Test
	public void givenExistingRestaurantId_whenGettingRestaurantById_thenRestaurantIsReturned() {
		// Given
		Long existingId = 1L;
		Restaurant validRestaurant = createValidRestaurant();
		when(restaurantRepository.findById(existingId)).thenReturn(java.util.Optional.of(validRestaurant));
		//when
		Restaurant fetchedRestaurant = restaurantServiceImpl.getRestaurantById(existingId);

		// Then
		assertThat(fetchedRestaurant).usingRecursiveComparison().ignoringFields("id", "createdAt").isEqualTo(validRestaurant);
	}

	@Test
	public void givenNonExistingRestaurantId_whenGettingRestaurantById_thenReturnNull() {
		// Given
		Long nonExistingId = 2L;
		when(restaurantRepository.findById(nonExistingId)).thenReturn(java.util.Optional.empty());
		//when
		Restaurant fetchedRestaurant = restaurantServiceImpl.getRestaurantById(nonExistingId);

		// Then
		assertNull(fetchedRestaurant);
	}

	@Test
	public void givenExistingRestaurantId_whenDeletingRestaurantById_thenRestaurantIsDeleted() {
		// Given
		Long existingId = 1L;
		when(restaurantRepository.existsById(existingId)).thenReturn(true);
		//when
		restaurantServiceImpl.deleteRestaurantById(existingId);

		// Then
		verify(restaurantRepository, times(1)).deleteById(existingId);
	}

	@Test
	public void givenNonExistingRestaurantId_whenDeletingRestaurantById_thenExceptionIsThrown() {
		// Given
		Long nonExistingId = 2L;
		when(restaurantRepository.existsById(nonExistingId)).thenReturn(false);
		//when
		assertThrows(RestaurantNotFoundException.class, () -> restaurantServiceImpl.deleteRestaurantById(nonExistingId));
	}

	@Test
	public void givenExistingRestaurantName_whenDeletingRestaurantByName_thenRestaurantIsDeleted() {
		// Given
		String existingName = "Valid Restaurant Name";
		when(restaurantRepository.existsByName(existingName)).thenReturn(true);
		//when
		restaurantServiceImpl.deleteRestaurantByName(existingName);

		// Then
		verify(restaurantRepository, times(1)).deleteByName(existingName);
	}

	@Test
	public void givenNonExistingRestaurantName_whenDeletingRestaurantByName_thenExceptionIsThrown() {
		// Given
		String nonExistingName = "Invalid Restaurant Name";
		when(restaurantRepository.existsByName(nonExistingName)).thenReturn(false);
		//when
		assertThrows(RestaurantNotFoundException.class, () -> restaurantServiceImpl.deleteRestaurantByName(nonExistingName));
	}

	private Restaurant createValidRestaurant() {
		boolean wheelchairAccessibleEntrance = true;
		return Restaurant.builder()
				.name(validRestaurantName)
				.address(validRestaurantAddress)
				.phone(validPhoneNumber)
				.location(validLocation)
				.googleMapsUrl(validGoogleMapsUrl)
				.websiteUrl(validWebsiteUrl)
				.openingHours(openingHours)
				.priceLevel(priceLevel)
				.wheelchairAccessibleEntrance(wheelchairAccessibleEntrance)
				.createdAt(LocalDateTime.now()).build();
	}

	private PlaceDetails createValidPlaceDetails() {
		PlaceDetails placeDetails = new PlaceDetails();
		placeDetails.name = validRestaurantName;
		placeDetails.formattedAddress = validRestaurantAddress;
		placeDetails.formattedPhoneNumber = validPhoneNumber;
		placeDetails.geometry = new Geometry();
		placeDetails.geometry.location = new LatLng();
		placeDetails.geometry.location.lat = validLocation.getX();
		placeDetails.geometry.location.lng = validLocation.getY();
		placeDetails.url = validGoogleMapsUrl;
		placeDetails.website = validWebsiteUrl;
		placeDetails.openingHours = openingHours;
		placeDetails.priceLevel = priceLevel;
		placeDetails.wheelchairAccessibleEntrance = true;
		return placeDetails;
	}

}
