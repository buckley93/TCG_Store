package com.yourco.tcg.catalog;

import com.yourco.tcg.catalog.dto.ProductResponse;
import com.yourco.tcg.common.PageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO (class):
 * - GET /products, GET /products/{id}, GET /sets.
 *
 * Annotations / types: @RestController
 */

@RestController
@RequestMapping("/products")
public class CatalogController {

    private final ProductSearchService productSearchService;

    public CatalogController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @GetMapping
    public PageResponse<ProductResponse> list(
            @RequestParam(required = false) ProductType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return productSearchService.list(type, page, size);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productSearchService.get(id);
    }

}
