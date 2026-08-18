package ee.nikolas.marketpulse.dto;

import ee.nikolas.marketpulse.entity.Marketplace;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TrackedKeywordRequestDto(

        @NotBlank(message = "Keyword must not be blank")
        String keyword,

        Marketplace marketplace,

        @Min(value = 1, message = "Search limit must be at least 1")
        @Max(value = 100, message = "Search limit must not exceed 100")
        Integer searchLimit
) {
}
