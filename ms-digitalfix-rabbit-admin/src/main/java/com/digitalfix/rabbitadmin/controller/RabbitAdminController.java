package com.digitalfix.rabbitadmin.controller;

import java.util.Map;

import com.digitalfix.rabbitadmin.dto.BindingRequest;
import com.digitalfix.rabbitadmin.dto.ExchangeRequest;
import com.digitalfix.rabbitadmin.dto.QueueRequest;
import com.digitalfix.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping("/queues")
    public ResponseEntity<Map<String, String>> createQueue(@Valid @RequestBody QueueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createQueue(request));
    }

    @DeleteMapping("/queues/{name}")
    public Map<String, String> deleteQueue(@PathVariable String name) {
        return service.deleteQueue(name);
    }

    @PostMapping("/exchanges")
    public ResponseEntity<Map<String, String>> createExchange(@Valid @RequestBody ExchangeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createExchange(request));
    }

    @DeleteMapping("/exchanges/{name}")
    public Map<String, String> deleteExchange(@PathVariable String name) {
        return service.deleteExchange(name);
    }

    @PostMapping("/bindings")
    public ResponseEntity<Map<String, String>> createBinding(@Valid @RequestBody BindingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBinding(request));
    }
}
