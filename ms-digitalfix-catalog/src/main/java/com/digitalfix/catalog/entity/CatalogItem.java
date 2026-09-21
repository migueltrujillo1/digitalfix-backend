package com.digitalfix.catalog.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "catalog_items")
public class CatalogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private Boolean active = true;

    public CatalogItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("nombre")
    public String getName() {
        return name;
    }

    @JsonProperty("nombre")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("descripcion")
    public String getDescription() {
        return description;
    }

    @JsonProperty("descripcion")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("tipo")
    public String getType() {
        if ("SERVICE".equals(type)) return "SERVICIO";
        if ("SPARE_PART".equals(type)) return "REPUESTO";
        return type;
    }

    @JsonProperty("tipo")
    public void setType(String type) {
        if (type == null) {
            this.type = null;
        } else if ("SERVICIO".equalsIgnoreCase(type) || "SERVICE".equalsIgnoreCase(type)) {
            this.type = "SERVICE";
        } else if ("REPUESTO".equalsIgnoreCase(type) || "SPARE_PART".equalsIgnoreCase(type)) {
            this.type = "SPARE_PART";
        } else {
            this.type = type;
        }
    }

    @JsonIgnore
    public String getTypeInterno() {
        return type;
    }

    @JsonProperty("precio")
    public BigDecimal getPrice() {
        return price;
    }

    @JsonProperty("precio")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @JsonProperty("stock")
    public Integer getStock() {
        return stock;
    }

    @JsonProperty("stock")
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @JsonProperty("activo")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("activo")
    public void setActive(Boolean active) {
        this.active = active;
    }
}