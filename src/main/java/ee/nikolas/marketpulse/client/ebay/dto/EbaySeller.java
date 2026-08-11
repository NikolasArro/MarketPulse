package ee.nikolas.marketpulse.client.ebay.dto;

public record EbaySeller(
        String username,
        String feedbackPercentage,
        Integer feedbackScore,
        String sellerAccountType
) {
}