package ee.nikolas.marketpulse.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TrackedKeywordSummaryDto(
        Long id,
        String keyword,
        boolean active,
        LocalDateTime lastSearchedAt,

        long trackedProducts,
        long risingProducts,
        long fallingProducts,
        long stableProducts,
        long notEnoughDataProducts,

        List<ProductTrendResponseDto> topTrending
) {
}
