package com.yourco.tcg.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * TODO (interface):
 * - findByCode(...).
 *
 * Annotations / types: extends JpaRepository
 */
public interface CardSetRepository extends JpaRepository<CardSet, Long> {
    Optional<CardSet> findByCode(String code);
}
