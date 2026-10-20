package com.yourco.tcg.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

/**
 * TODO (interface):
 * - Query methods + spec-based filtering.
 *
 * Annotations / types: extends JpaRepository, JpaSpecificationExecutor
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @EntityGraph(attributePaths = {"cardSet"})
    Page<Product> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cardSet"})
    Page<Product> findAllByType(ProductType type, Pageable pageable);

    @EntityGraph(attributePaths = {"cardSet"})
    Optional<Product> findWithCardSetById(Long id);
}
