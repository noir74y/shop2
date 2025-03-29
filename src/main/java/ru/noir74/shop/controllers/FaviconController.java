package ru.noir74.shop.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaviconController {
    @GetMapping(value = "favicon.ico", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> favicon() {
        Resource resource = new ClassPathResource("static/favicon.ico");
        return ResponseEntity.ok(resource);
    }
}
