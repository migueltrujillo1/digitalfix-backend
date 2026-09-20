package com.digitalfix.catalog.controller;

import com.digitalfix.catalog.entity.CatalogItem;
import com.digitalfix.catalog.service.CatalogService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/services")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CatalogItem> create(
            @RequestBody CatalogItem item) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(item));
    }

    @GetMapping
    public ResponseEntity<List<CatalogItem>> getAll(
            @RequestParam(required = false) String type) {

        return ResponseEntity.ok(service.findAll(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogItem> getById(
            @PathVariable Long id) {

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogItem> update(
            @PathVariable Long id,
            @RequestBody CatalogItem item) {

        return service.update(id, item)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }
}