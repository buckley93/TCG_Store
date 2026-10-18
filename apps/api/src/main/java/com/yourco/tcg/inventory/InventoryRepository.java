package com.yourco.tcg.inventory;

/**
 * TODO (interface):
 * - Atomic decrement: UPDATE ... SET quantity = quantity - :n WHERE id = :id AND quantity >= :n.
 * - Return rows updated (0 = out of stock).
 *
 * Annotations / types: extends JpaRepository; use @Modifying @Query
 */
public interface InventoryRepository {
}
