package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.dto.ProductSnapshotResponseDto;
import ee.nikolas.marketpulse.dto.ProductTrendResponseDto;
import ee.nikolas.marketpulse.service.ProductService;
import ee.nikolas.marketpulse.service.ProductSnapshotService;
import ee.nikolas.marketpulse.service.ProductTrendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(
        name = "Products",
        description = "Product search, popularity, history and trend analytics"
)
public class ProductController {

    private final ProductService productService;
    private final ProductSnapshotService productSnapshotService;
    private final ProductTrendService productTrendService;

    @Operation(summary = "Get all stored products")
    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Add product")
    @PostMapping
    public ProductResponseDto addProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        return productService.saveProduct(productRequestDto);
    }

    @Operation(summary = "Search products on eBay and store snapshots")
    @GetMapping("/search")
    public List<ProductResponseDto> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return productService.searchProducts(query, limit);
    }

    @Operation(summary = "Get products sorted by popularity")
    @GetMapping("/popular")
    public List<ProductResponseDto> getPopularProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return productService.getPopularProducts(limit);
    }

    @Operation(summary = "Get product price and ranking history")
    @GetMapping("/{id}/history")
    public List<ProductSnapshotResponseDto> getProductHistory(
            @PathVariable Long id
    ) {
        return productSnapshotService.getHistory(id);
    }

    @Operation(summary = "Get trending products for a search query")
    @GetMapping("/trending")
    public List<ProductTrendResponseDto> getTrendingProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return productTrendService
                .getTrendingProducts(limit, query);
    }

    @Operation(summary = "Get trend analysis for a product")
    @GetMapping("/{id}/trend")
    public ProductTrendResponseDto getProductTrend(
            @PathVariable Long id,
            @RequestParam String query
    ) {
        return productTrendService.getTrend(id, query);
    }
}