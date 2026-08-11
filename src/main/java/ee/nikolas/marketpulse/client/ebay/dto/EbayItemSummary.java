package ee.nikolas.marketpulse.client.ebay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EbayItemSummary(

        String itemId,

        String title,

        EbayImage image,

        EbayMoney price,

        EbaySeller seller,

        String condition,

        String conditionId,

        String itemWebUrl,

        EbayItemLocation itemLocation,

        Instant itemCreationDate,

        Boolean topRatedBuyingExperience,

        Boolean priorityListing,

        Boolean availableCoupons
) {
}