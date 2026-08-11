package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.client.ebay.EbayClient;
import ee.nikolas.marketpulse.client.ebay.dto.EbaySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ebay")
@RequiredArgsConstructor
public class EbayController {

    private final EbayClient ebayClient;

    @GetMapping("/search")
    public EbaySearchResponse search(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {

        return ebayClient.search(query, limit);
    }
}