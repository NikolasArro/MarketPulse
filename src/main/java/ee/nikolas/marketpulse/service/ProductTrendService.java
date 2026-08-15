package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.ProductTrendResponseDto;
import ee.nikolas.marketpulse.entity.Product;
import ee.nikolas.marketpulse.entity.ProductSnapshot;
import ee.nikolas.marketpulse.model.TrendDirection;
import ee.nikolas.marketpulse.repository.ProductRepository;
import ee.nikolas.marketpulse.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTrendService {

    private final ProductRepository productRepository;
    private final ProductSnapshotRepository snapshotRepository;

    public ProductTrendResponseDto getTrend(Long productId, String query) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found: " + productId
                        )
                );

        List<ProductSnapshot> snapshots =
                snapshotRepository
                        .findTop2ByProductIdAndSearchQuery_QueryOrderByCapturedAtDesc(
                                productId,
                                query
                        );

        if (snapshots.size() < 2) {
            return createNotEnoughDataResponse(
                    product,
                    snapshots
            );
        }

        ProductSnapshot current = snapshots.get(0);
        ProductSnapshot previous = snapshots.get(1);

        Integer positionChange =
                calculatePositionChange(
                        current.getSearchPosition(),
                        previous.getSearchPosition()
                );

        Double popularityChange =
                calculatePopularityChange(
                        current.getPopularityScore(),
                        previous.getPopularityScore()
                );

        BigDecimal priceChangePercent =
                calculatePriceChangePercent(
                        current.getPrice(),
                        previous.getPrice()
                );

        double trendScore =
                calculateTrendScore(
                        positionChange,
                        popularityChange,
                        priceChangePercent
                );

        TrendDirection trend =
                determineTrend(
                        positionChange,
                        popularityChange
                );

        return new ProductTrendResponseDto(
                product.getId(),
                product.getTitle(),

                current.getPrice(),
                previous.getPrice(),
                priceChangePercent,

                current.getSearchPosition(),
                previous.getSearchPosition(),
                positionChange,

                current.getPopularityScore(),
                previous.getPopularityScore(),
                popularityChange,
                trendScore,

                trend
        );
    }

    private Integer calculatePositionChange(
            Integer currentPosition,
            Integer previousPosition
    ) {

        if (currentPosition == null || previousPosition == null) {
            return null;
        }

        return previousPosition - currentPosition;
    }

    private Double calculatePopularityChange(
            Double currentScore,
            Double previousScore
    ) {

        if (currentScore == null || previousScore == null) {
            return null;
        }

        double change = currentScore - previousScore;

        return Math.round(change * 100.0) / 100.0;
    }

    private BigDecimal calculatePriceChangePercent(
            BigDecimal currentPrice,
            BigDecimal previousPrice
    ) {

        if (currentPrice == null
                || previousPrice == null
                || previousPrice.compareTo(BigDecimal.ZERO) == 0) {

            return null;
        }

        return currentPrice
                .subtract(previousPrice)
                .divide(
                        previousPrice,
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private TrendDirection determineTrend(
            Integer positionChange,
            Double popularityChange
    ) {

        if (positionChange == null || popularityChange == null) {
            return TrendDirection.STABLE;
        }

        if (positionChange >= 2 && popularityChange > 0.5) {
            return TrendDirection.RISING;
        }

        if (positionChange <= -2 && popularityChange < -0.5) {
            return TrendDirection.FALLING;
        }

        return TrendDirection.STABLE;
    }

    private ProductTrendResponseDto
    createNotEnoughDataResponse(
            Product product,
            List<ProductSnapshot> snapshots
    ) {

        ProductSnapshot current = snapshots.isEmpty()
                                    ? null
                                    : snapshots.getFirst();

        return new ProductTrendResponseDto(
                product.getId(),
                product.getTitle(),

                current != null
                        ? current.getPrice()
                        : product.getPrice(),

                null,
                null,

                current != null
                        ? current.getSearchPosition()
                        : product.getSearchPosition(),

                null,
                null,

                current != null
                        ? current.getPopularityScore()
                        : product.getPopularityScore(),

                null,
                null,
                0.0,

                TrendDirection.NOT_ENOUGH_DATA
        );
    }

    private double calculateTrendScore(
            Integer positionChange,
            Double popularityChange,
            BigDecimal priceChangePercent
    ) {

        double score = 0.0;

        if (positionChange != null) {
            score += positionChange * 5.0;
        }

        if (popularityChange != null) {
            score += popularityChange * 3.0;
        }

        if (priceChangePercent != null) {
            /*
             * Снижение цены считаем небольшим позитивным фактором.
             * -10% цены -> +5 trend points
             */
            score += priceChangePercent
                    .negate()
                    .doubleValue()
                    * 0.5;
        }

        return Math.round(score * 100.0) / 100.0;
    }

    public List<ProductTrendResponseDto> getTrendingProducts(
            int limit,
            String query
    ) {

        int safeLimit = Math.clamp(limit, 1, 100);

        return productRepository
                .findAll()
                .stream()
                .map(product -> getTrend(product.getId(), query))
                .filter(trend ->
                        trend.trend()
                                != TrendDirection.NOT_ENOUGH_DATA
                )
                .sorted(
                        (a, b) ->
                                Double.compare(
                                        b.trendScore(),
                                        a.trendScore()
                                )
                )
                .limit(safeLimit)
                .toList();
    }
}