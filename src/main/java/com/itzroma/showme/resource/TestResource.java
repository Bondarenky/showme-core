package com.itzroma.showme.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestResource {
    @GetMapping("/all")
    public String all() {
        return "Hi stranger!";
    }
}
