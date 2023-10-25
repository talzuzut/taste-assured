package com.restaurant.utils.config;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class GoogleMapsConfig {
	private final String googleMapsApiKey = System.getenv("GOOGLE_MAPS_API_KEY");

	@Bean
	public GeoApiContext geoApiContext() {

		if (googleMapsApiKey == null) {
			throw new RuntimeException("GOOGLE_MAPS_API_KEY environment variable is not set.");
		}
		return new GeoApiContext.Builder()
				.apiKey(googleMapsApiKey)
//			.maxRetries(3)
				.build();
	}
}

