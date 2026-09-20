package com.digitalfix.workorders.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    public record DataDto(LocalDateTime timestamp, String message) {}

    @GetMapping("/home")
    public ResponseEntity<DataDto> getData() {
        DataDto data = new DataDto(
            LocalDateTime.now(),
            "Backend funcionando"
        );

        return ResponseEntity.ok(data);
    }
}