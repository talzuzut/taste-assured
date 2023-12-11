package com.user_management.repository;

import com.user_management.model.FollowRelationship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRelationshipRepository extends JpaRepository<FollowRelationship, Long> {

	@Query("SELECT f.followingId FROM FollowRelationship f WHERE f.followedId = :followingId")
	Page<Long> findFollowersByFollowingId(Long followingId, Pageable pageable);

	@Query("SELECT f.followedId FROM FollowRelationship f WHERE f.followingId = :followingId")
	Page<Long> findFollowedUsersByUserId(Long followingId, Pageable pageable);

	@Query("SELECT f  FROM FollowRelationship f where f.followingId = :followingId AND f.followedId= :followedId ")
	FollowRelationship findByFollowingIdAndFollowedId(Long followedId, Long followingId);

	@Query("DELETE FROM FollowRelationship f WHERE f.followingId = :userId OR f.followedId = :userId")
	void deleteFollowRelationshipsByUserId(Long userId);
}