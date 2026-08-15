package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.model.TrendDirection;

import java.math.BigDecimal;

public record ProductTrendResponseDto(
        Long productId,
        String title,

        BigDecimal currentPrice,
        BigDecimal previousPrice,
        BigDecimal priceChangePercent,

        Integer currentPosition,
        Integer previousPosition,
        Integer positionChange,

        Double currentPopularityScore,
        Double previousPopularityScore,
        Double popularityScoreChange,

        Double trendScore,

        TrendDirection trend
) {
}
