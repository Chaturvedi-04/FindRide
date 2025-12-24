package com.alpha.FindRide.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "FindRide is running on Railway";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
