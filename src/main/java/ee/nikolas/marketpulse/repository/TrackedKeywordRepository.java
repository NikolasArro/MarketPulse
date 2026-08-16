package ee.nikolas.marketpulse.repository;

import ee.nikolas.marketpulse.entity.TrackedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackedKeywordRepository extends JpaRepository<TrackedKeyword, Long> {

    List<TrackedKeyword> findByActiveTrue();

    boolean existsByKeywordIgnoreCase(String keyword);
}
