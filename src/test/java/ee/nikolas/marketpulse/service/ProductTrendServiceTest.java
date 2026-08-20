package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.ProductTrendResponseDto;
import ee.nikolas.marketpulse.entity.Product;
import ee.nikolas.marketpulse.entity.ProductSnapshot;
import ee.nikolas.marketpulse.exception.ProductNotFoundException;
import ee.nikolas.marketpulse.model.TrendDirection;
import ee.nikolas.marketpulse.repository.ProductRepository;
import ee.nikolas.marketpulse.repository.ProductSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTrendServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSnapshotRepository snapshotRepository;

    private ProductTrendService service;

    private Product product;

    @BeforeEach
    void setUp() {

        service = new ProductTrendService(
                productRepository,
                snapshotRepository
        );

        product = new Product();
        product.setId(1L);
        product.setTitle("Wireless Headphones");
        product.setPrice(new BigDecimal("90.00"));
        product.setSearchPosition(5);
        product.setPopularityScore(80.0);
    }

    @Test
    void shouldReturnRisingTrend() {

        ProductSnapshot current = snapshot(
                "90.00",
                5,
                80.0
        );

        ProductSnapshot previous = snapshot(
                "100.00",
                10,
                70.0
        );

        mockProductAndSnapshots(
                List.of(current, previous)
        );

        ProductTrendResponseDto result =
                service.getTrend(
                        1L,
                        "wireless headphones"
                );

        assertEquals(
                TrendDirection.RISING,
                result.trend()
        );

        assertEquals(
                5,
                result.positionChange()
        );

        assertEquals(
                10.0,
                result.popularityScoreChange()
        );

        assertEquals(
                new BigDecimal("-10.00"),
                result.priceChangePercent()
        );

        /*
         * trendScore:
         *
         * position:   5 * 5     = 25
         * popularity: 10 * 3    = 30
         * price:      -10%      = +5
         *
         * total = 60
         */
        assertEquals(
                60.0,
                result.trendScore()
        );
    }

    @Test
    void shouldReturnFallingTrend() {

        ProductSnapshot current = snapshot(
                "110.00",
                10,
                65.0
        );

        ProductSnapshot previous = snapshot(
                "100.00",
                5,
                75.0
        );

        mockProductAndSnapshots(
                List.of(current, previous)
        );

        ProductTrendResponseDto result =
                service.getTrend(
                        1L,
                        "wireless headphones"
                );

        assertEquals(
                TrendDirection.FALLING,
                result.trend()
        );

        assertEquals(
                -5,
                result.positionChange()
        );

        assertEquals(
                -10.0,
                result.popularityScoreChange()
        );

        assertEquals(
                new BigDecimal("10.00"),
                result.priceChangePercent()
        );

        /*
         * position:    -5 * 5  = -25
         * popularity: -10 * 3 = -30
         * price:       +10%    = -5
         *
         * total = -60
         */
        assertEquals(
                -60.0,
                result.trendScore()
        );
    }

    @Test
    void shouldReturnStableTrendForSmallChanges() {

        ProductSnapshot current = snapshot(
                "99.00",
                4,
                80.4
        );

        ProductSnapshot previous = snapshot(
                "100.00",
                5,
                80.0
        );

        mockProductAndSnapshots(
                List.of(current, previous)
        );

        ProductTrendResponseDto result =
                service.getTrend(
                        1L,
                        "wireless headphones"
                );

        assertEquals(
                TrendDirection.STABLE,
                result.trend()
        );

        assertEquals(
                1,
                result.positionChange()
        );

        assertEquals(
                0.4,
                result.popularityScoreChange()
        );
    }

    @Test
    void shouldReturnNotEnoughDataWhenOnlyOneSnapshotExists() {

        ProductSnapshot current = snapshot(
                "90.00",
                5,
                80.0
        );

        mockProductAndSnapshots(
                List.of(current)
        );

        ProductTrendResponseDto result =
                service.getTrend(
                        1L,
                        "wireless headphones"
                );

        assertEquals(
                TrendDirection.NOT_ENOUGH_DATA,
                result.trend()
        );

        assertEquals(
                0.0,
                result.trendScore()
        );

        assertNull(
                result.previousPrice()
        );

        assertNull(
                result.previousPosition()
        );
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.getTrend(
                        999L,
                        "wireless headphones"
                )
        );

        verify(snapshotRepository, never())
                .findTop2ByProductIdAndSearchQuery_QueryOrderByCapturedAtDesc(
                        anyLong(),
                        anyString()
                );
    }

    private ProductSnapshot snapshot(
            String price,
            int position,
            double popularityScore
    ) {

        ProductSnapshot snapshot =
                new ProductSnapshot();

        snapshot.setProduct(product);
        snapshot.setPrice(
                new BigDecimal(price)
        );
        snapshot.setSearchPosition(position);
        snapshot.setPopularityScore(
                popularityScore
        );

        return snapshot;
    }

    private void mockProductAndSnapshots(
            List<ProductSnapshot> snapshots
    ) {

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(snapshotRepository
                .findTop2ByProductIdAndSearchQuery_QueryOrderByCapturedAtDesc(
                        1L,
                        "wireless headphones"
                ))
                .thenReturn(snapshots);
    }
}