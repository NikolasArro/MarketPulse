package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.ProductTrendResponseDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordSummaryDto;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.TrackedKeyword;
import ee.nikolas.marketpulse.exception.DuplicateTrackedKeywordException;
import ee.nikolas.marketpulse.exception.TrackedKeywordNotFoundException;
import ee.nikolas.marketpulse.model.TrendDirection;
import ee.nikolas.marketpulse.repository.TrackedKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackedKeywordService {

    private final TrackedKeywordRepository repository;
    private final ProductTrendService productTrendService;

    public List<TrackedKeywordResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public TrackedKeywordResponseDto create(
            TrackedKeywordRequestDto request
    ) {
        String keyword = request.keyword().trim();

        if (repository.existsByKeywordIgnoreCase(keyword)) {
            throw new DuplicateTrackedKeywordException(keyword);
        }

        TrackedKeyword trackedKeyword = getTrackedKeyword(request, keyword);

        return toDto(
                repository.save(trackedKeyword)
        );
    }

    private static @NonNull TrackedKeyword getTrackedKeyword(TrackedKeywordRequestDto request, String keyword) {
        TrackedKeyword trackedKeyword =
                new TrackedKeyword();

        trackedKeyword.setKeyword(keyword);

        trackedKeyword.setMarketplace(
                request.marketplace() != null
                        ? request.marketplace()
                        : Marketplace.EBAY
        );

        trackedKeyword.setSearchLimit(
                request.searchLimit() != null
                        ? request.searchLimit()
                        : 20
        );

        trackedKeyword.setActive(true);
        return trackedKeyword;
    }

    public TrackedKeywordResponseDto setActive(
            Long id,
            boolean active
    ) {
        TrackedKeyword trackedKeyword = findById(id);

        trackedKeyword.setActive(active);

        return toDto(repository.save(trackedKeyword));
    }

    public void delete(Long id) {
        TrackedKeyword trackedKeyword = findById(id);

        repository.delete(trackedKeyword);
    }

    private TrackedKeyword findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new TrackedKeywordNotFoundException(id)
                );
    }

    private TrackedKeywordResponseDto toDto(
            TrackedKeyword entity
    ) {
        return new TrackedKeywordResponseDto(
                entity.getId(),
                entity.getKeyword(),
                entity.getMarketplace(),
                entity.isActive(),
                entity.getSearchLimit(),
                entity.getLastSearchedAt()
        );
    }

    public TrackedKeywordSummaryDto getSummary(Long id) {

        TrackedKeyword trackedKeyword = findById(id);

        List<ProductTrendResponseDto> trends =
                productTrendService.getAllTrendsForQuery(
                        trackedKeyword.getKeyword()
                );

        long rising = trends.stream()
                .filter(trend ->
                        trend.trend() == TrendDirection.RISING
                )
                .count();

        long falling = trends.stream()
                .filter(trend ->
                        trend.trend() == TrendDirection.FALLING
                )
                .count();

        long stable = trends.stream()
                .filter(trend ->
                        trend.trend() == TrendDirection.STABLE
                )
                .count();

        long notEnoughData = trends.stream()
                .filter(trend ->
                        trend.trend() == TrendDirection.NOT_ENOUGH_DATA
                )
                .count();

        List<ProductTrendResponseDto> topTrending =
                trends.stream()
                        .filter(trend ->
                                trend.trend()
                                        != TrendDirection.NOT_ENOUGH_DATA
                        )
                        .sorted(
                                (a, b) ->
                                        Double.compare(
                                                b.trendScore(),
                                                a.trendScore()
                                        )
                        )
                        .limit(5)
                        .toList();

        return new TrackedKeywordSummaryDto(
                trackedKeyword.getId(),
                trackedKeyword.getKeyword(),
                trackedKeyword.isActive(),
                trackedKeyword.getLastSearchedAt(),

                trends.size(),
                rising,
                falling,
                stable,
                notEnoughData,

                topTrending
        );
    }
}