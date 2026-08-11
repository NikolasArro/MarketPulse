package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.client.ebay.EbayClient;
import ee.nikolas.marketpulse.client.ebay.dto.EbayItemSummary;
import ee.nikolas.marketpulse.client.ebay.dto.EbaySearchResponse;
import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.Product;
import ee.nikolas.marketpulse.mapper.EbayProductMapper;
import ee.nikolas.marketpulse.mapper.ProductMapper;
import ee.nikolas.marketpulse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final EbayClient ebayClient;
    private final EbayProductMapper ebayProductMapper;

    private final PopularityScoreService popularityScoreService;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    public ProductResponseDto saveProduct(
            ProductRequestDto dto
    ) {

        Product product = productMapper.toEntity(dto);
        product.setFetchedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);

        return productMapper.toResponseDto(savedProduct);
    }

    public List<ProductResponseDto> searchProducts(String query, int limit) {
        EbaySearchResponse response = ebayClient.search(query, limit);

        if (response == null || response.itemSummaries() == null) {
            return List.of();
        }

        List<ProductResponseDto> result = new ArrayList<>();

        int position = 1;

        for (EbayItemSummary item : response.itemSummaries()) {
            Product product = saveOrUpdateEbayProduct(item, position);

            result.add(productMapper.toResponseDto(product));

            position++;
        }

        return result;
    }

    private Product saveOrUpdateEbayProduct(EbayItemSummary item, int searchPosition) {
        Product product = productRepository
                            .findByExternalIdAndMarketplace(item.itemId(), Marketplace.EBAY)
                            .orElseGet(
                                    () -> ebayProductMapper
                                            .toProduct(item, searchPosition)
                            );

//        Kui Product juba olemas, uuendame andmed.

        if (item.seller() != null) {
            if (item.seller().feedbackPercentage() != null) {
                product.setSellerFeedbackPercentage(Double.valueOf(item.seller().feedbackPercentage())
                );
            }

            product.setSellerFeedbackScore(item.seller().feedbackScore());
            product.setSellerAccountType(item.seller().sellerAccountType());
        }

        product.setConditionId(item.conditionId());

        if (item.itemLocation() != null) {
            product.setItemCountry(item.itemLocation().country());
        }

        product.setTopRatedBuyingExperience(item.topRatedBuyingExperience());
        product.setPriorityListing(item.priorityListing());
        product.setAvailableCoupons(item.availableCoupons());
        product.setItemCreationDate(item.itemCreationDate());
        product.setTitle(item.title());

        if (item.price() != null) {
            product.setPrice(new java.math.BigDecimal(item.price().value()));
            product.setCurrency(item.price().currency());
        }

        product.setProductUrl(item.itemWebUrl());

        if (item.image() != null) {
            product.setImageUrl(item.image().imageUrl());
        }

        product.setSearchPosition(searchPosition);
        product.setFetchedAt(LocalDateTime.now());

        product.setPopularityScore(
                popularityScoreService.calculate(product)
        );

        return productRepository.save(product);
    }
}