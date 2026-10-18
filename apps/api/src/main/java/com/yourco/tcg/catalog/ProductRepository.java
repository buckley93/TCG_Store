package com.yourco.tcg.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO (interface):
 * - Query methods + spec-based filtering.
 *
 * Annotations / types: extends JpaRepository, JpaSpecificationExecutor
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
