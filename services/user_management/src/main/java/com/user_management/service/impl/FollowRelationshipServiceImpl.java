package com.user_management.service.impl;

import com.user_management.exception.InvalidFollowRelationshipArgument;
import com.user_management.model.CreateFollowRelationshipDTO;
import com.user_management.model.FollowRelationship;
import com.user_management.repository.FollowRelationshipRepository;
import com.user_management.service.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowRelationshipServiceImpl implements com.user_management.service.api.FollowRelationshipService {
	private final FollowRelationshipRepository followRelationshipRepository;
	private final UserService userService;

	@Override
	public Page<Long> getFollowersByUserId(Long id, Pageable pageable) {
		log.info("Retrieving followers for user: {}", id);
		return followRelationshipRepository.findFollowersByFollowingId(id, pageable);
	}


	@Override
	public Page<Long> getFollowedUsersByUserId(Long id, Pageable pageable) {
		log.info("Retrieving followed users for user: {}", id);
		return followRelationshipRepository.findFollowedUsersByUserId(id, pageable);
	}

	@Override
	public void addFollowRelationship(CreateFollowRelationshipDTO followRelationshipDTO) {
		log.info("Adding follow relationship: {}", followRelationshipDTO);
		if (!validateFollowRelationship(followRelationshipDTO)) {
			throw new InvalidFollowRelationshipArgument();
		}
		FollowRelationship followRelationship = convertToFollowRelationship(followRelationshipDTO);
		followRelationshipRepository.save(followRelationship);
	}

	private FollowRelationship convertToFollowRelationship(CreateFollowRelationshipDTO followRelationshipDTO) {
		return new FollowRelationship(followRelationshipDTO.getFollowingId(), followRelationshipDTO.getFollowedId());
	}

	private boolean validateFollowRelationship(CreateFollowRelationshipDTO followRelationshipDTO) {
		Long followedId = followRelationshipDTO.getFollowedId();
		return userService.userExistsById(followRelationshipDTO.getFollowingId()) &&
				userService.userExistsById(followedId) &&
				!Objects.equals(followRelationshipDTO.getFollowingId(), followedId);
	}

	@Override
	public void deleteFollowRelationshipsByUserId(Long userId) {
		log.info("Deleting follow relationship: {}", userId);
		try {
			followRelationshipRepository.deleteFollowRelationshipsByUserId(userId);
		} catch (DataAccessException e) {
			log.error("Error deleting follow relationships for user {}: {}", userId, e.getMessage());
			throw new RuntimeException("Could not delete follow relationships for user " + userId, e);
		}
	}

	@Override
	public FollowRelationship getFollowRelationshipDetailsByIds(Long followingId, Long followedId) {
		log.info("Retrieving follow relationship details for followingId: {} and followedId: {}", followingId, followedId);
		return followRelationshipRepository.findByFollowingIdAndFollowedId(followedId, followingId);
	}

	@Override
	public void batchAddFollowRelationship() {
		log.info("Adding follow relationships for demo users");
		List<FollowRelationship> followRelationships = new LinkedList<>();
		for (int i = 1; i <= 90; i++) {
			for (int j = 2; j <= 10; j++) {
				followRelationships.add(new FollowRelationship((long) i, (long) j));
/*                FollowRelationship followRelationship = new FollowRelationship((long) i, (long) j);
                followRelationshipRepository.save(followRelationship);*/
			}
			followRelationshipRepository.saveAll(followRelationships);

		}
	}
}