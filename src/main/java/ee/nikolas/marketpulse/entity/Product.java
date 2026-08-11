package ee.nikolas.marketpulse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"externalId", "marketplace"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Marketplace marketplace;

    @Column(nullable = false)
    private String title;

    private BigDecimal price;

    private String currency;

    @Column(length = 2000)
    private String productUrl;

    @Column(length = 2000)
    private String imageUrl;

    private Integer searchPosition;

    private Double popularityScore;

    private LocalDateTime fetchedAt;

    private Double sellerFeedbackPercentage;

    private Integer sellerFeedbackScore;

    private String sellerAccountType;

    private String conditionId;

    private String itemCountry;

    private Boolean topRatedBuyingExperience;

    private Boolean priorityListing;

    private Boolean availableCoupons;

    private Instant itemCreationDate;
}
