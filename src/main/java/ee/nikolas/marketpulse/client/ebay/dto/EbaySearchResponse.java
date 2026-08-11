package ee.nikolas.marketpulse.client.ebay.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EbaySearchResponse(

        Integer total,

        Integer limit,

        Integer offset,

        List<EbayItemSummary> itemSummaries
) {
}