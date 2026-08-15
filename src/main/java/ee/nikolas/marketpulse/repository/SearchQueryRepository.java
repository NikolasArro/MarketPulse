package ee.nikolas.marketpulse.repository;

import ee.nikolas.marketpulse.entity.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchQueryRepository
        extends JpaRepository<SearchQuery, Long> {
}