package com.user_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",
		uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
	@SequenceGenerator(name = "seqGen", sequenceName = "user_id_seq", allocationSize = 1)
	private Long id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private LocalDateTime createdAt;
}
