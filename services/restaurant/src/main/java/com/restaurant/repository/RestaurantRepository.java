package com.restaurant.repository;

import com.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	//	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Restaurant r WHERE r.name = ?1")
	boolean existsByName(String name);

	@Query("SELECT r FROM Restaurant r WHERE r.name = ?1")
	void deleteByName(String name);
}
