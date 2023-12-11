package com.user_management.model;


import java.util.List;

public record RatingWithFollowersList(String ratingId, List<Long> followers) {
}
