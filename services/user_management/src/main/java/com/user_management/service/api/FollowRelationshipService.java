package com.user_management.service.api;

import com.user_management.model.CreateFollowRelationshipDTO;
import com.user_management.model.FollowRelationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRelationshipService {
	Page<Long> getFollowersByUserId(Long id, Pageable pageable);

	Page<Long> getFollowedUsersByUserId(Long id, Pageable pageable);

	void addFollowRelationship(CreateFollowRelationshipDTO followRelationshipDTO);

	void deleteFollowRelationshipsByUserId(Long userId);

	FollowRelationship getFollowRelationshipDetailsByIds(Long followingId, Long followedId);

	void batchAddFollowRelationship();
}
