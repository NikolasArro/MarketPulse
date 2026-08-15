package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.dto.ProductSnapshotResponseDto;
import ee.nikolas.marketpulse.service.ProductService;
import ee.nikolas.marketpulse.service.ProductSnapshotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductSnapshotService productSnapshotService;

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ProductResponseDto addProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        return productService.saveProduct(productRequestDto);
    }

    @GetMapping("/search")
    public List<ProductResponseDto> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return productService.searchProducts(query, limit);
    }

    @GetMapping("/popular")
    public List<ProductResponseDto> getPopularProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return productService.getPopularProducts(limit);
    }

    @GetMapping("/{id}/history")
    public List<ProductSnapshotResponseDto> getProductHistory(
            @PathVariable Long id
    ) {
        return productSnapshotService.getHistory(id);
    }
}