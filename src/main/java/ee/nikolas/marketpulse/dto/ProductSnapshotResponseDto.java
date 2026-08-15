package ee.nikolas.marketpulse.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductSnapshotResponseDto(
        Long id,
        BigDecimal price,
        Integer searchPosition,
        Double popularityScore,
        LocalDateTime capturedAt
) {
}