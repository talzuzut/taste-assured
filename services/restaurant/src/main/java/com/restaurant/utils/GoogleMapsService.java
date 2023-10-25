package com.restaurant.utils;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class GoogleMapsService {
	private final GeoApiContext context;

	@Async
	public CompletableFuture<PlaceDetails> getPlaceDetailsByQueryAsync(String query) throws IOException, InterruptedException, ApiException {
		PlacesSearchResponse response = PlacesApi.textSearchQuery(context, query).await();
		if (response.results.length > 0) {
			return CompletableFuture.completedFuture(PlacesApi.placeDetails(context, response.results[0].placeId).await());
		}
		return CompletableFuture.completedFuture(null);
	}
}
