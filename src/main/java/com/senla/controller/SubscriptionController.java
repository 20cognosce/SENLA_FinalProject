package com.senla.controller;

import com.senla.controller.dto.update.SubscriptionUpdateDto;
import com.senla.controller.mapper.SubscriptionMapper;
import com.senla.model.entity.Subscription;
import com.senla.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @GetMapping("/{id}")
    public Subscription getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getById(id);
    }

    @GetMapping
    public List<Subscription> getAllSubscription() {
        return subscriptionService.getAll(new HashMap<>(), "id", true, 10);
    }

    @PatchMapping("/{id}")
    public void updateModelsForSubscription(@PathVariable Long id, @RequestBody SubscriptionUpdateDto updateDto) {
        Subscription subscription = subscriptionService.getById(id);
        Subscription updatedSubscription = subscriptionMapper.convertToSubscription(updateDto);
        subscriptionService.updateEntityFromDto(subscription, updatedSubscription, Subscription.class);
        subscriptionService.update(subscription);
    }
}