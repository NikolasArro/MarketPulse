package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;

import java.time.LocalDateTime;

public record ProductResponseDto(
        Long id,
        String externalId,
        Marketplace marketplace,
        String title,
        Double price,
        String currency,
        String productUrl,
        String imageUrl,
        Integer searchPosition,
        Double popularityScore,
        LocalDateTime fetchedAt
) {
}