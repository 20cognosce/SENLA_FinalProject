package com.senla.controller.restcontroller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class DefaultController {

    /**
     * Default entrypoint which returns indicator message that everything works right
     * @return Success indicator message
     * */

    @GetMapping
    public ResponseEntity<String> getSuccessMessage() throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "I've just sent successful response to you at " + LocalDateTime.now());
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
    }
}
