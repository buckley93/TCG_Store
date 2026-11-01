package com.yourco.tcg.catalog.dto;

import com.yourco.tcg.catalog.Product;
import com.yourco.tcg.catalog.ProductSubType;
import com.yourco.tcg.catalog.ProductType;

import java.util.Map;

/**
 * TODO (record):
 * - Product fields + available stock summary (min price, count).
 */
public record ProductResponse(
        Long id,
        ProductType type,
        ProductSubType subtype,
        String name,
        String description,
        String setCode,
        String setName,
        String cardNumber,
        String rarity,
        String imageUrl,
        Map<String, Object> attributes
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getType(),
                product.getSubtype(),
                product.getName(),
                product.getDescription(),
                product.getCardSet() != null ? product.getCardSet().getCode() : null,
                product.getCardSet() != null ? product.getCardSet().getName() : null,
                product.getCardNumber(),
                product.getRarity(),
                product.getImageUrl(),
                product.getAttributes()
        );
    }
}
