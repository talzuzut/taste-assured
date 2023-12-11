package com.user_management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_management.model.RatingDetails;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


public class RatingDetailsDeserializer implements Deserializer<RatingDetails> {
	ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public RatingDetails deserialize(String s, byte[] bytes) {
		try {
			if (bytes == null) {
				return null;
			}

			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(bytes, RatingDetails.class);
		} catch (Exception e) {
			throw new SerializationException("Error deserializing RatingWithFollowersList ", e);
		}
	}
}
