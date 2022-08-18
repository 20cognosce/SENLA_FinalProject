package com.senla.controller;


import lombok.RequiredArgsConstructor;
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
@RequestMapping(value = "/v1", produces = {"application/json; charset=UTF-8"})
@RestController
public class DefaultController {

    @GetMapping
    public String getReadme(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletContext().getRealPath("/WEB-INF/classes/doc/README.md");
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }
}
