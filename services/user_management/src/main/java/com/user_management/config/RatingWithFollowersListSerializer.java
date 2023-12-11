package com.user_management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_management.model.RatingWithFollowersList;
import org.apache.kafka.common.serialization.Serializer;
import org.hibernate.type.SerializationException;

public class RatingWithFollowersListSerializer implements Serializer<RatingWithFollowersList> {
	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String s, RatingWithFollowersList ratingWithFollowersList) {

		try {
			if (ratingWithFollowersList == null) {
				return null;
			}
			return objectMapper.writeValueAsBytes(ratingWithFollowersList);
		} catch (Exception e) {
			throw new SerializationException("Error serializing RatingWithFollowersList ", e);
		}
	}
}
