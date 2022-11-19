package com.senla.controller.restcontroller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
@RequestMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class DefaultController {

    /**
     * Default entrypoint which returns indicator message that everything works right
     * @return Greeting message
     * */

    @GetMapping
    public ResponseEntity<String> getSuccessMessage() {
        return ResponseEntity.ok("I'm running!");
    }
}
