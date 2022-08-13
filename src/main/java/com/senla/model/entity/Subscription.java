package com.senla.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "price")
    Float price;
    @Column(name = "discount")
    Float discount;

    @Column(name = "start_time")
    LocalDateTime startTime;
    @Column(name = "end_time")
    LocalDateTime endTime;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "subscription")
    User user;

    public boolean isValid() {
        return LocalDateTime.now().isBefore(endTime);
    }
}
