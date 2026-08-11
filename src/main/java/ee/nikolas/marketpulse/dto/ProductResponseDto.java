package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

public record ProductResponseDto(
        Long id,
        String externalId,
        Marketplace marketplace,
        String title,
        BigDecimal price,
        String currency,
        String productUrl,
        String imageUrl,
        Integer searchPosition,
        Double popularityScore,
        LocalDateTime fetchedAt,
        Double sellerFeedbackPercentage,
        Integer sellerFeedbackScore,
        String sellerAccountType,
        String conditionId,
        String itemCountry,
        Boolean topRatedBuyingExperience,
        Boolean priorityListing,
        Boolean availableCoupons,
        Instant itemCreationDate
) {
}