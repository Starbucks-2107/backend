package com.starbucks.backend.global.doc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/docs")
public class RESTDocController {
    @GetMapping(value = "")
    public String getApiDocs() {
        return "redirect:http://localhost:63342/backend/backend/src/main/resources/static/index.html";
    }
}
