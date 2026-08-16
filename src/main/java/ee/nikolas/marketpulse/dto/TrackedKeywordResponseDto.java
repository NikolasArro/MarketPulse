package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;

import java.time.LocalDateTime;

public record TrackedKeywordResponseDto(
        Long id,
        String keyword,
        Marketplace marketplace,
        boolean active,
        Integer searchLimit,
        LocalDateTime lastSearchedAt
) {
}
