package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.TrackedKeyword;
import ee.nikolas.marketpulse.repository.TrackedKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackedKeywordService {

    private final TrackedKeywordRepository repository;

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

        if (keyword.isBlank()) {
            throw new IllegalArgumentException(
                    "Keyword cannot be empty"
            );
        }

        if (repository.existsByKeywordIgnoreCase(keyword)) {
            throw new IllegalArgumentException(
                    "Keyword is already tracked: " + keyword
            );
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
                request.searchLimit() != null ? Math.clamp(request.searchLimit(), 1, 100) : 20
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
                        new IllegalArgumentException(
                                "Tracked keyword not found: " + id
                        )
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
}