package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.entity.Product;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class PopularityScoreService {

    public double calculate(Product product) {

        double searchPositionScore = calculateSearchPositionScore(product.getSearchPosition());

        double sellerFeedbackScore = calculateSellerFeedbackScore(product.getSellerFeedbackPercentage());

        double sellerActivityScore = calculateSellerActivityScore(product.getSellerFeedbackScore());

        double topRatedScore =
                Boolean.TRUE.equals(product.getTopRatedBuyingExperience()) ? 100.0 : 0.0;

        double freshnessScore = calculateFreshnessScore(product.getItemCreationDate());

        double score =
                searchPositionScore * 0.35 +
                        sellerFeedbackScore * 0.25 +
                        sellerActivityScore * 0.20 +
                        topRatedScore * 0.10 +
                        freshnessScore * 0.10;

        return Math.round(score * 100.0) / 100.0;
    }

    private double calculateSearchPositionScore(Integer position) {

        if (position == null || position <= 0) {return 0.0;}

        return Math.max(0.0, 100.0 - (position - 1) * 5.0);
    }

    private double calculateSellerFeedbackScore(Double feedbackPercentage) {

        if (feedbackPercentage == null) return 0.0;

        return Math.min(100.0, feedbackPercentage);
    }

    private double calculateSellerActivityScore(
            Integer feedbackScore
    ) {

        if (feedbackScore == null || feedbackScore <= 0) {
            return 0.0;
        }

        /*
         * scale
         *
         * 10 feedback    -> ~25
         * 100            -> ~50
         * 1 000          -> ~75
         * 10 000+        -> 100
         */

        double score = Math.log10(feedbackScore + 1) / 4.0 * 100.0;

        return Math.min(100.0, score);
    }

    private double calculateFreshnessScore(Instant creationDate) {

        if (creationDate == null) return 0.0;

        long days = Duration.between(creationDate, Instant.now()).toDays();

        if (days <= 7) return 100.0;

        if (days <= 30) return 80.0;

        if (days <= 90) return 60.0;

        if (days <= 180) return 40.0;

        if (days <= 365) return 20.0;

        return 10.0;
    }
}