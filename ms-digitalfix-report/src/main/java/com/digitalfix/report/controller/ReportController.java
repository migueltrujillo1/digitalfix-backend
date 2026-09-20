package com.digitalfix.report.controller;

import com.digitalfix.report.dto.KpiResponse;
import com.digitalfix.report.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service = service; }

    @PreAuthorize("hasAuthority('SCOPE_access_as_user')")
    @GetMapping("/kpis")
    public ResponseEntity<KpiResponse> getKpis(@RequestParam(defaultValue = "last24h") String range) {
        return ResponseEntity.ok(service.getKpis(range));
    }
}
