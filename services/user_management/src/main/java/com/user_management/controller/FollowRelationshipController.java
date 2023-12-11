package com.user_management.controller;

import com.user_management.model.CreateFollowRelationshipDTO;
import com.user_management.model.FollowRelationship;
import com.user_management.service.impl.FollowRelationshipServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/follow")
@AllArgsConstructor
public class FollowRelationshipController {
	private final FollowRelationshipServiceImpl followRelationshipService;

	@GetMapping("/{id}/followers")
	public Page<Long> getFollowersByUserId(@PathVariable Long id, Pageable pageable) {
		log.info("Followers of the user {} requested", id);
		return followRelationshipService.getFollowersByUserId(id, pageable);
	}

	@GetMapping("/{id}/following")
	public Page<Long> getFollowedUsersByUserId(@PathVariable Long id, Pageable pageable) {
		log.info("Users followed by the user {} requested", id);
		return followRelationshipService.getFollowedUsersByUserId(id, pageable);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Void> addFollowRelationship(@RequestBody CreateFollowRelationshipDTO followRelationshipDTO) {
		followRelationshipService.addFollowRelationship(followRelationshipDTO);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/demo_follow")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Void> addFollowRelationship() {
		log.info("Adding follow relationships for demo users");
		followRelationshipService.batchAddFollowRelationship();
		return ResponseEntity.ok().build();
	}

/*    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteFollowRelationship(@PathVariable Long userId) {
        followRelationshipService.deleteFollowRelationshipsByUserId(userId);
        return ResponseEntity.ok().build();
    }*/

	@GetMapping("/following/{followingId}/followed/{followedId}")
	public ResponseEntity<FollowRelationship> getFollowRelationshipDetails(@PathVariable Long followingId, @PathVariable Long followedId) {
		FollowRelationship followRelationship = followRelationshipService.getFollowRelationshipDetailsByIds(followingId, followedId);
		return ResponseEntity.ok(followRelationship);
	}
}