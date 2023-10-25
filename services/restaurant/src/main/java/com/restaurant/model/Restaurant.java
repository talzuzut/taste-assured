package com.restaurant.model;

import com.google.maps.model.OpeningHours;
import com.google.maps.model.PriceLevel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurant_account")
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
	@SequenceGenerator(name = "seqGen", sequenceName = "restaurant_account_id_seq", allocationSize = 1)
	private Long id;
	private String name;
	private String address;
	private String phone;
	private BigDecimal longitude;
	private BigDecimal latitude;
	private URL googleMapsUrl;
	private URL websiteUrl;
	private OpeningHours openingHours;
	private PriceLevel priceLevel;
	private Boolean wheelchairAccessibleEntrance;
	private LocalDateTime createdAt;

}
