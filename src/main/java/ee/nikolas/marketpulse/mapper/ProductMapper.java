package ee.nikolas.marketpulse.mapper;

import ee.nikolas.marketpulse.dto.ProductRequestDto;
import ee.nikolas.marketpulse.dto.ProductResponseDto;
import ee.nikolas.marketpulse.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDto dto);

    ProductResponseDto toResponseDto(Product product);
}
