package com.example.bookmark.api;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RequestMapping("/")
@RestController
public class MainRestController {

    @GetMapping
    String index() {
        return "<html><body>" +
                "<ul>" +
                "<li><a href=\"v3/api-docs\" target=\"_blank\">API Docs</a></li>" +
                "<li><a href=\"swagger-ui.html\" target=\"_blank\">Swagger</a></li>" +
                "</ul>" +
                "</body></html>";
    }

}
