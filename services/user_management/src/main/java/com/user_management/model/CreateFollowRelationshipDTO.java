package com.user_management.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreateFollowRelationshipDTO {
	@NotBlank
	@Positive
	public Long followingId;
	@NotBlank
	@Positive
	public Long followedId;
}
