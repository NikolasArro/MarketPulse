package ee.nikolas.marketpulse.client.ebay;

import ee.nikolas.marketpulse.client.ebay.dto.EbaySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class EbayClient {

    private static final String SEARCH_URL =
            "https://api.ebay.com/buy/browse/v1/item_summary/search";

    private final RestTemplate restTemplate;
    private final EbayAuthService ebayAuthService;

    @Value("${ebay.marketplace-id}")
    private String marketplaceId;

    public EbaySearchResponse search(
            String query,
            int limit
    ) {

        URI uri = UriComponentsBuilder
                .fromUriString(SEARCH_URL)
                .queryParam("q", query)
                .queryParam("limit", limit)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(
                ebayAuthService.getAccessToken()
        );

        headers.set(
                "X-EBAY-C-MARKETPLACE-ID",
                marketplaceId
        );

        HttpEntity<Void> request =
                new HttpEntity<>(headers);

        ResponseEntity<EbaySearchResponse> response =
                restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        request,
                        EbaySearchResponse.class
                );

        return response.getBody();
    }
}