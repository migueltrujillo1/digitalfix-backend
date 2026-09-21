package com.digitalfix.workorders.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String status;

    private String technician;

    private LocalDateTime createdAt;

    public WorkOrder() {
    }

    public WorkOrder(String description, String status, String technician) {
        this.description = description;
        this.status = status;
        this.technician = technician;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (status == null) {
            status = "CREADA";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("descripcion")
    public String getDescription() {
        return description;
    }

    @JsonProperty("descripcion")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("estado")
    public String getStatus() {
        return status;
    }

    @JsonProperty("estado")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("tecnico")
    public String getTechnician() {
        return technician;
    }

    @JsonProperty("tecnico")
    public void setTechnician(String technician) {
        this.technician = technician;
    }

    @JsonProperty("fechaCreacion")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("fechaCreacion")
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}