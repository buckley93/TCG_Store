package com.yourco.tcg.catalog;

import com.yourco.tcg.catalog.dto.ProductResponse;
import com.yourco.tcg.common.NotFoundException;
import com.yourco.tcg.common.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO (class):
 * - Combine specs, pagination, sorting.
 *
 * Annotations / types: @Service
 */

@Service
@Transactional(readOnly = true)
public class ProductSearchService {

    private final ProductRepository products;

    public ProductSearchService(ProductRepository products) {
        this.products = products;
    }

    public PageResponse<ProductResponse> list(ProductType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by("name"));
        var result = (type == null)
                ? products.findAll(pageable)
                : products.findAllByType(type, pageable);
        return PageResponse.from(result.map(ProductResponse::from));
    }

    public ProductResponse get(Long id) {
        return products.findWithCardSetById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
    }

}
