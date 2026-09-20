package com.digitalfix.report.dto;

public record KpiResponse(
    String range,
    long totalWorkOrders,
    long created,
    long assigned,
    long inTransit,
    long inProgress,
    long closed,
    long cancelled
) {}
