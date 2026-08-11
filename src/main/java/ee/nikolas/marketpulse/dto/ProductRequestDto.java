package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequestDto(

        @NotBlank
        String externalId,

        @NotNull
        Marketplace marketplace,

        @NotBlank
        String title,

        @PositiveOrZero
        BigDecimal price,

        String currency,

        String productUrl,

        String imageUrl,

        @PositiveOrZero
        Integer searchPosition,

        @PositiveOrZero
        Double popularityScore
) {
}
