package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;

public record TrackedKeywordRequestDto(
        String keyword,
        Marketplace marketplace,
        Integer searchLimit
) {
}
