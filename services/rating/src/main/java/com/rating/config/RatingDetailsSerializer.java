package com.rating.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rating.model.RatingDetails;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class RatingDetailsSerializer implements Serializer<RatingDetails> {
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String s, RatingDetails ratingDetails) {

		try {
			if (ratingDetails == null) {
				return null;
			}
			return objectMapper.writeValueAsBytes(ratingDetails);
		} catch (Exception e) {
			throw new SerializationException("Error serializing RestaurantRatingDTO ", e);
		}
	}
}
