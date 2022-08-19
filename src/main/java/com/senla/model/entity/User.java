package com.senla.model.entity;


import com.senla.model.entityenum.Role;
import com.senla.model.entityenum.UserAccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "login")
    String login;
    @Column(name = "hash_password")
    String hashPassword;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    Role role;

    @Column(name = "status")
    UserAccountStatus status;
    @Column(name = "name")
    String name;
    @Column(name = "phone")
    String phone; //TODO: regex, validation
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth; //TODO: >18

    @Column(name = "credit_card")
    String creditCard; //TODO: encryption
    @JoinColumn(name = "tariff_id")
    @ManyToOne(fetch = FetchType.EAGER)
    Tariff tariff;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    User2Subscription user2Subscription;
}
