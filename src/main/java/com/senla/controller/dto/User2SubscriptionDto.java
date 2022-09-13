package com.senla.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class User2SubscriptionDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SubscriptionDto subscription;
}
