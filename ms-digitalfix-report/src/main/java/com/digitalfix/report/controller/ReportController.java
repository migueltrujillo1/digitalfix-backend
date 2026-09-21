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
    public ResponseEntity<KpiResponse> getKpis(
            @RequestParam(required = false) String range,
            @RequestParam(required = false) String periodo) {
        String valor = range != null ? range : convertirPeriodo(periodo);
        return ResponseEntity.ok(service.getKpis(valor));
    }

    private String convertirPeriodo(String periodo) {
        if (periodo == null || periodo.isBlank()) return "last24h";
        return switch (periodo.toLowerCase()) {
            case "ultimos7dias", "ultimos_7_dias" -> "last7d";
            case "ultimos30dias", "ultimos_30_dias" -> "last30d";
            default -> "last24h";
        };
    }
}
