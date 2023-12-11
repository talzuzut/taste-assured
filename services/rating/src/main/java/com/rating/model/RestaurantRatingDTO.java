package com.rating.model;


public record RestaurantRatingDTO(Long restaurantId, Long recommenderId, Integer rating, String comment) {
}