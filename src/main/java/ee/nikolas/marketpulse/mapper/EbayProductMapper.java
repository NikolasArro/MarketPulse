package ee.nikolas.marketpulse.mapper;

import ee.nikolas.marketpulse.client.ebay.dto.EbayItemSummary;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class EbayProductMapper {

    public Product toProduct(
            EbayItemSummary item,
            int searchPosition
    ) {

        Product product = new Product();

        product.setExternalId(item.itemId());
        product.setMarketplace(Marketplace.EBAY);
        product.setTitle(item.title());

        if (item.price() != null) {
            product.setPrice(new BigDecimal(item.price().value()));

            product.setCurrency(item.price().currency());
        }

        product.setProductUrl(item.itemWebUrl());

        if (item.image() != null) {
            product.setImageUrl(item.image().imageUrl());
        }

        product.setSearchPosition(searchPosition);
        product.setFetchedAt(LocalDateTime.now());

        if (item.seller() != null) {

            if (item.seller().feedbackPercentage() != null) {
                product.setSellerFeedbackPercentage(
                        Double.valueOf(item.seller().feedbackPercentage())
                );
            }

            product.setSellerFeedbackScore(item.seller().feedbackScore());
            product.setSellerAccountType(item.seller().sellerAccountType());
        }

        product.setConditionId(item.conditionId());

        if (item.itemLocation() != null) {
            product.setItemCountry(item.itemLocation().country());
        }

        product.setTopRatedBuyingExperience(item.topRatedBuyingExperience());
        product.setPriorityListing(item.priorityListing());
        product.setAvailableCoupons(item.availableCoupons());
        product.setItemCreationDate(item.itemCreationDate());

        return product;
    }
}