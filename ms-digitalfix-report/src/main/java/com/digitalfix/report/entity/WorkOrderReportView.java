package com.digitalfix.report.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_orders")
public class WorkOrderReportView {
    @Id
    private Long id;
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected WorkOrderReportView() {}
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
