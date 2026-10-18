package com.digitalfix.notify.controller;

import com.digitalfix.notify.entity.AuditEvent;
import com.digitalfix.notify.service.AuditEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditEventService service;

    public AuditController(AuditEventService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @GetMapping
    public List<AuditEventResponse> latest(@RequestParam(defaultValue = "100") int limit) {
        if (limit < 1 || limit > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit debe estar entre 1 y 500");
        }
        return service.latest(limit).stream()
                .map(event -> new AuditEventResponse(
                        event.getId(),
                        event.getOrderId(),
                        event.getPayload(),
                        event.getCreatedAt()))
                .toList();
    }

    public record AuditEventResponse(long id, long orderId, String payload, LocalDateTime createdAt) {
    }
}
