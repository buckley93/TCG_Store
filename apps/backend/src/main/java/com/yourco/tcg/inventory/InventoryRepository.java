package com.yourco.tcg.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * TODO (interface):
 * - Atomic decrement: UPDATE ... SET quantity = quantity - :n WHERE id = :id AND quantity >= :n.
 * - Return rows updated (0 = out of stock).
 *
 * Annotations / types: extends JpaRepository; use @Modifying @Query
 */

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    List<InventoryItem> findByProductIdAndStatus(Long productId, InventoryStatus status);

    @Modifying
    @Query("UPDATE InventoryItem i SET i.quantity = i.quantity - :qty " +
           "WHERE i.id = :id AND i.quantity >= :qty")
    int decrementQuantity(@Param("id") Long id, @Param("qty") int qty);

}
