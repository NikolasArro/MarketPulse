package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequestDto(

        @NotBlank
        String externalId,

        @NotNull
        Marketplace marketplace,

        @NotBlank
        String title,

        @PositiveOrZero
        Double price,

        String currency,

        String productUrl,

        String imageUrl,

        @PositiveOrZero
        Integer searchPosition,

        @PositiveOrZero
        Double popularityScore
) {
}
