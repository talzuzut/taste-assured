package com.user_management.service;

import com.user_management.exception.InvalidFollowRelationshipArgument;
import com.user_management.model.CreateFollowRelationshipDTO;
import com.user_management.model.FollowRelationship;
import com.user_management.repository.FollowRelationshipRepository;
import com.user_management.service.api.UserService;
import com.user_management.service.impl.FollowRelationshipServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowRelationshipServiceTest {

	@Mock
	private FollowRelationshipRepository followRelationshipRepository;

	@Mock
	private UserService userService;
	@InjectMocks
	private FollowRelationshipServiceImpl followRelationshipService;

/*
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        followRelationshipService = new FollowRelationshipServiceImpl(followRelationshipRepository, userService);
    }
*/

	@Test
	public void testAddFollowRelationship() {
		CreateFollowRelationshipDTO dto = CreateFollowRelationshipDTO.builder().followedId(2L).followingId(1L).build();
		when(userService.userExistsById(1L)).thenReturn(true);
		when(userService.userExistsById(2L)).thenReturn(true);

		followRelationshipService.addFollowRelationship(dto);

		verify(followRelationshipRepository, times(1)).save(any(FollowRelationship.class));
	}

	@Test
	public void testAddFollowRelationshipInvalid() {
		CreateFollowRelationshipDTO dto = CreateFollowRelationshipDTO.builder().followedId(1L).followingId(1L).build();
		when(userService.userExistsById(1L)).thenReturn(true);

		assertThrows(InvalidFollowRelationshipArgument.class, () -> followRelationshipService.addFollowRelationship(dto));

		verify(followRelationshipRepository, times(0)).save(any(FollowRelationship.class));
	}

	@Test
	public void testGetFollowersByUserId() {
		Long userId = 1L;
		Pageable pageable = mock(Pageable.class);

		followRelationshipService.getFollowersByUserId(userId, pageable);

		verify(followRelationshipRepository, times(1)).findFollowersByFollowingId(userId, pageable);
	}

	@Test
	public void testGetFollowedUsersByUserId() {
		Long userId = 1L;
		Pageable pageable = mock(Pageable.class);

		followRelationshipService.getFollowedUsersByUserId(userId, pageable);

		verify(followRelationshipRepository, times(1)).findFollowedUsersByUserId(userId, pageable);
	}

	@Test
	public void testDeleteFollowRelationshipsByUserId() {
		Long userId = 1L;

		followRelationshipService.deleteFollowRelationshipsByUserId(userId);

		verify(followRelationshipRepository, times(1)).deleteFollowRelationshipsByUserId(userId);
	}

	@Test
	public void testGetFollowRelationshipDetailsByIds() {
		Long followingId = 1L;
		Long followedId = 2L;

		followRelationshipService.getFollowRelationshipDetailsByIds(followingId, followedId);

		verify(followRelationshipRepository, times(1)).findByFollowingIdAndFollowedId(followedId, followingId);
	}
}