package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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
}