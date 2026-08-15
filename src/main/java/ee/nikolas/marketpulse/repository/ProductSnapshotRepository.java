package ee.nikolas.marketpulse.repository;

import ee.nikolas.marketpulse.entity.ProductSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSnapshotRepository
        extends JpaRepository<ProductSnapshot, Long> {

    List<ProductSnapshot>
    findByProductIdOrderByCapturedAtAsc(Long productId);

    List<ProductSnapshot>
    findTop2ByProductIdOrderByCapturedAtDesc(Long productId);
}