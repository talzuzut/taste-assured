package com.rating.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_management.model.RatingWithFollowersList;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class RatingWithFollowersListDeserializer implements Deserializer<RatingWithFollowersList> {
	@Override
	public RatingWithFollowersList deserialize(String s, byte[] bytes) {
		try {
			if (bytes == null) {
				return null;
			}

			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(bytes, RatingWithFollowersList.class);
		} catch (Exception e) {
			throw new SerializationException("Error deserializing RatingWithFollowersList ", e);
		}

	}
}
