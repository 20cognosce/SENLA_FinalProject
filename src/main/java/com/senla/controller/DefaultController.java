package com.senla.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
     * Default entrypoint which returns String of README.md file, which is stored in src\main\resources\doc\README.md
     * @param request implicitly got HttpServletRequest
     * @param response implicitly got HttpServletResponse
     * @throws IOException if README.md not found
     * @return content of README.md
     * */

    //TODO: to refactor
    @GetMapping
    public String getReadme(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletContext().getRealPath("/WEB-INF/classes/doc/README.md");
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }
}
