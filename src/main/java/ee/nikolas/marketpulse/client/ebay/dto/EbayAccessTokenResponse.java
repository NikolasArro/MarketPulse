package ee.nikolas.marketpulse.client.ebay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EbayAccessTokenResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("token_type")
        String tokenType
) {
}