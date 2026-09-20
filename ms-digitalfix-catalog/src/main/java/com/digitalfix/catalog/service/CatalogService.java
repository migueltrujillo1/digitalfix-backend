package com.digitalfix.catalog.service;

import com.digitalfix.catalog.entity.CatalogItem;
import com.digitalfix.catalog.repository.CatalogItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    private final CatalogItemRepository repository;

    public CatalogService(CatalogItemRepository repository) {
        this.repository = repository;
    }

    public CatalogItem create(CatalogItem item) {

        if (!item.getType().equals("SERVICE")
                && !item.getType().equals("SPARE_PART")) {
            throw new IllegalArgumentException(
                    "El tipo debe ser SERVICE o SPARE_PART"
            );
        }

        if (item.getStock() == null) {
            item.setStock(0);
        }

        if (item.getActive() == null) {
            item.setActive(true);
        }

        return repository.save(item);
    }

    public List<CatalogItem> findAll(String type) {

        if (type != null && !type.isBlank()) {
            return repository.findByType(type);
        }

        return repository.findAll();
    }

    public Optional<CatalogItem> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<CatalogItem> update(Long id, CatalogItem data) {

        return repository.findById(id).map(item -> {

            item.setName(data.getName());
            item.setDescription(data.getDescription());
            item.setType(data.getType());
            item.setPrice(data.getPrice());
            item.setStock(data.getStock());
            item.setActive(data.getActive());

            return repository.save(item);
        });
    }
}