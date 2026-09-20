package com.digitalfix.report.service;

import com.digitalfix.report.dto.KpiResponse;
import com.digitalfix.report.entity.WorkOrderReportView;
import com.digitalfix.report.repository.WorkOrderReportRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    private final WorkOrderReportRepository repository;
    public ReportService(WorkOrderReportRepository repository) { this.repository = repository; }

    public KpiResponse getKpis(String range) {
        String normalizedRange = (range == null || range.isBlank()) ? "last24h" : range;
        LocalDateTime from = switch (normalizedRange) {
            case "last7d" -> LocalDateTime.now().minusDays(7);
            case "last30d" -> LocalDateTime.now().minusDays(30);
            case "last24h" -> LocalDateTime.now().minusHours(24);
            default -> throw new IllegalArgumentException("range debe ser last24h, last7d o last30d");
        };
        List<WorkOrderReportView> orders = repository.findByCreatedAtGreaterThanEqual(from);
        return new KpiResponse(normalizedRange, orders.size(), count(orders,"CREADA"), count(orders,"ASIGNADA"),
            count(orders,"EN_DESPLAZAMIENTO"), count(orders,"EN_EJECUCIÓN"), count(orders,"CERRADA"), count(orders,"CANCELADA"));
    }

    private long count(List<WorkOrderReportView> orders, String status) {
        return orders.stream().filter(o -> status.equals(o.getStatus())).count();
    }
}
