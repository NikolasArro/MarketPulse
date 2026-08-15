package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.ProductSnapshotResponseDto;
import ee.nikolas.marketpulse.entity.Product;
import ee.nikolas.marketpulse.entity.ProductSnapshot;
import ee.nikolas.marketpulse.entity.SearchQuery;
import ee.nikolas.marketpulse.repository.ProductSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSnapshotService {

    private final ProductSnapshotRepository snapshotRepository;

    public void createSnapshot(Product product, SearchQuery searchQuery) {

        ProductSnapshot snapshot =
                new ProductSnapshot();

        snapshot.setProduct(product);
        snapshot.setSearchQuery(searchQuery);
        snapshot.setPrice(product.getPrice());
        snapshot.setSearchPosition(
                product.getSearchPosition()
        );
        snapshot.setPopularityScore(
                product.getPopularityScore()
        );
        snapshot.setCapturedAt(
                LocalDateTime.now()
        );

        snapshotRepository.save(snapshot);
    }

    public List<ProductSnapshotResponseDto> getHistory(
            Long productId
    ) {
        return snapshotRepository
                .findByProductIdOrderByCapturedAtAsc(productId)
                .stream()
                .map(snapshot ->
                        new ProductSnapshotResponseDto(
                                snapshot.getId(),
                                snapshot.getPrice(),
                                snapshot.getSearchPosition(),
                                snapshot.getPopularityScore(),
                                snapshot.getCapturedAt()
                        )
                )
                .toList();
    }
}