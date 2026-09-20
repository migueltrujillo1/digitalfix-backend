package com.digitalfix.catalog.repository;

import com.digitalfix.catalog.entity.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {

    List<CatalogItem> findByType(String type);
}