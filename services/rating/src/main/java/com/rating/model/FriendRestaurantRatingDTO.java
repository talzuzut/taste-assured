package com.rating.model;

public record FriendRestaurantRatingDTO(Long restaurantId,
                                        Long recommenderId,
                                        Long targetId,
                                        Integer rating,
                                        String comment) {
}
