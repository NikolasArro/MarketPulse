package ee.nikolas.marketpulse.scheduler;

import ee.nikolas.marketpulse.entity.TrackedKeyword;
import ee.nikolas.marketpulse.repository.TrackedKeywordRepository;
import ee.nikolas.marketpulse.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductTrackingScheduler {

    private final TrackedKeywordRepository trackedKeywordRepository;
    private final ProductService productService;

    @Scheduled(
            cron = "${marketpulse.tracking.cron:0 0 */6 * * *}",
            zone = "UTC"
    )
    public void collectProductData() {

        List<TrackedKeyword> keywords = trackedKeywordRepository.findByActiveTrue();

        log.info(
                "Starting scheduled product tracking. Keywords: {}",
                keywords.size()
        );

        for (TrackedKeyword trackedKeyword : keywords) {

            try {

                log.info(
                        "Tracking keyword: {}",
                        trackedKeyword.getKeyword()
                );

                productService.searchProducts(
                        trackedKeyword.getKeyword(),
                        trackedKeyword.getSearchLimit()
                );

                trackedKeyword.setLastSearchedAt(
                        LocalDateTime.now()
                );

                trackedKeywordRepository.save(
                        trackedKeyword
                );

            } catch (Exception e) {

                log.error(
                        "Failed to track keyword: {}",
                        trackedKeyword.getKeyword(),
                        e
                );
            }
        }
    }

    @PostConstruct
    public void init() {
        log.info("ProductTrackingScheduler initialized");
    }
}