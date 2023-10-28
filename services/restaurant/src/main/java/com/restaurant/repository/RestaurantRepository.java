package com.restaurant.repository;

import com.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	boolean existsByName(String name);

	void deleteByName(String name);

	@Query("SELECT r FROM Restaurant r WHERE r.name = ?1")
	Restaurant getByName(String name);
}
