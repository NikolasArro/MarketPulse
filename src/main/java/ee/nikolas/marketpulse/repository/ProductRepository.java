package ee.nikolas.marketpulse.repository;

import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByExternalIdAndMarketplace(
            String externalId,
            Marketplace marketplace
    );

    List<Product> findAllByOrderByPopularityScoreDesc(
            Pageable pageable
    );
}