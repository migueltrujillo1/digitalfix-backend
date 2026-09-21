package com.digitalfix.report.dto;

public record KpiResponse(
    String periodo,
    long totalOrdenes,
    long creadas,
    long asignadas,
    long enDesplazamiento,
    long enEjecucion,
    long cerradas,
    long canceladas
) {}
