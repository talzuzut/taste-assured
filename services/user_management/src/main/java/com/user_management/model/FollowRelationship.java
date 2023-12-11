package com.user_management.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "follow_relationships",
		uniqueConstraints = @UniqueConstraint(columnNames = {"following_id", "followed_id"})
)
@RequiredArgsConstructor
@Data
public class FollowRelationship {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "follow_relationships_id_seq", sequenceName = "follow_relationships_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "following_id")
	private Long followingId;

	@Column(name = "followed_id")
	private Long followedId;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "status")
	private Status status;

	public FollowRelationship(Long followingId, Long followedId) {
		this.followingId = followingId;
		this.followedId = followedId;
		this.createdAt = LocalDateTime.now();
		this.status = Status.ACCEPTED;
	}
}
