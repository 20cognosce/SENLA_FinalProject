package com.senla.model.entity;


import com.senla.model.enums.Role;
import com.senla.model.enums.UserAccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;
    String hashPassword;
    @Enumerated(EnumType.STRING)
    Role role;

    UserAccountStatus status;
    String name;
    String phoneNumber; // regex, validation
    LocalDate dateOfBirth; // >18

    String creditCard;
    @ManyToOne
    Tariff tariff;
    @OneToOne
    Subscription subscription;
}
