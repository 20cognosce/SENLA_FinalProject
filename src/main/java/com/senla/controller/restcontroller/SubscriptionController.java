package com.senla.controller.restcontroller;

import com.senla.controller.mapper.SubscriptionMapper;
import com.senla.domain.dto.SubscriptionDto;
import com.senla.domain.dto.creation.SubscriptionCreationDto;
import com.senla.domain.dto.update.SubscriptionUpdateDto;
import com.senla.domain.model.entity.Subscription;
import com.senla.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(value = "/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @GetMapping("/{id}")
    public SubscriptionDto getSubscriptionById(@PathVariable Long id) {
        return subscriptionMapper.convertToSubscriptionDto(subscriptionService.getById(id));
    }

    @GetMapping
    public List<SubscriptionDto> getAllSubscription() {
        return subscriptionService.getAll(new HashMap<>(), "id", true, 100)
                .stream().map(subscriptionMapper::convertToSubscriptionDto).collect(toList());
    }

    @PostMapping
    public void createSubscription(@RequestBody SubscriptionCreationDto creationDto) {
        Subscription subscription = subscriptionMapper.convertToSubscription(creationDto);
        subscriptionService.create(subscription);
    }

    @PatchMapping("/{id}")
    public void updateModelsForSubscription(@PathVariable Long id, @RequestBody SubscriptionUpdateDto updateDto) {
        Subscription subscription = subscriptionService.getById(id);
        Subscription updatedSubscription = subscriptionMapper.convertToSubscription(updateDto);
        subscriptionService.updateEntityFromDto(subscription, updatedSubscription, Subscription.class);
        subscriptionService.update(subscription);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSubscription(@PathVariable("id") Long id) {
        subscriptionService.deleteById(id);
    }
}
