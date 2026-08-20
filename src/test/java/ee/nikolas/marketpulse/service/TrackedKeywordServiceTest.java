package ee.nikolas.marketpulse.service;

import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.entity.TrackedKeyword;
import ee.nikolas.marketpulse.exception.DuplicateTrackedKeywordException;
import ee.nikolas.marketpulse.exception.TrackedKeywordNotFoundException;
import ee.nikolas.marketpulse.repository.TrackedKeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackedKeywordServiceTest {

    @Mock
    private TrackedKeywordRepository repository;

    @Mock
    private ProductTrendService productTrendService;

    private TrackedKeywordService service;

    @BeforeEach
    void setUp() {
        service = new TrackedKeywordService(
                repository,
                productTrendService
        );
    }

    @Test
    void shouldCreateTrackedKeyword() {

        TrackedKeywordRequestDto request =
                new TrackedKeywordRequestDto(
                        "wireless headphones",
                        Marketplace.EBAY,
                        20
                );

        when(repository.existsByKeywordIgnoreCase(
                "wireless headphones"
        )).thenReturn(false);

        when(repository.save(any(TrackedKeyword.class)))
                .thenAnswer(invocation -> {

                    TrackedKeyword keyword =
                            invocation.getArgument(0);

                    keyword.setId(1L);

                    return keyword;
                });

        TrackedKeywordResponseDto result =
                service.create(request);

        assertEquals(1L, result.id());
        assertEquals(
                "wireless headphones",
                result.keyword()
        );
        assertEquals(
                Marketplace.EBAY,
                result.marketplace()
        );
        assertEquals(20, result.searchLimit());
        assertTrue(result.active());

        verify(repository).save(
                any(TrackedKeyword.class)
        );
    }

    @Test
    void shouldThrowExceptionWhenKeywordAlreadyExists() {

        TrackedKeywordRequestDto request =
                new TrackedKeywordRequestDto(
                        "wireless headphones",
                        Marketplace.EBAY,
                        20
                );

        when(repository.existsByKeywordIgnoreCase(
                "wireless headphones"
        )).thenReturn(true);

        assertThrows(
                DuplicateTrackedKeywordException.class,
                () -> service.create(request)
        );

        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenTrackedKeywordNotFound() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TrackedKeywordNotFoundException.class,
                () -> service.delete(999L)
        );

        verify(repository, never())
                .delete(any());
    }

    @Test
    void shouldUseDefaultValues() {

        TrackedKeywordRequestDto request =
                new TrackedKeywordRequestDto(
                        "gaming mouse",
                        null,
                        null
                );

        when(repository.existsByKeywordIgnoreCase(
                "gaming mouse"
        )).thenReturn(false);

        when(repository.save(any(TrackedKeyword.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        TrackedKeywordResponseDto result =
                service.create(request);

        assertEquals(
                Marketplace.EBAY,
                result.marketplace()
        );

        assertEquals(
                20,
                result.searchLimit()
        );

        assertTrue(result.active());
    }
}