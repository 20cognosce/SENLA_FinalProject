package com.senla.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user2subscription")
public class User2Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;

	@Column(name = "discount")
	Float discount;
	@Column(name = "start_time")
	LocalDateTime startTime;
	@Column(name = "end_time")
	LocalDateTime endTime;

	@JoinColumn(name = "user_id")
	@OneToOne(fetch = FetchType.EAGER)
	User user;
	@JoinColumn(name = "subscription_id")
	@OneToOne(fetch = FetchType.EAGER)
	Subscription subscription;

	public boolean isValid() {
		return LocalDateTime.now().isBefore(endTime);
	}
}
