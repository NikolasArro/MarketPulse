package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.entity.Product;
import ee.nikolas.marketpulse.mapper.ProductMapper;
import ee.nikolas.marketpulse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public ProductResponseDto saveProduct(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);

        product.setFetchedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponseDto(savedProduct);
    }
}