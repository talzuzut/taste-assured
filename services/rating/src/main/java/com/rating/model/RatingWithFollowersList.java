package com.rating.model;

import java.util.List;

public record RatingWithFollowersList(String ratingId, List<Long> followers) {
}
