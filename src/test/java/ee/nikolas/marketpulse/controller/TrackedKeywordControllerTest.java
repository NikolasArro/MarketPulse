package ee.nikolas.marketpulse.controller;

import tools.jackson.databind.ObjectMapper;
import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.entity.Marketplace;
import ee.nikolas.marketpulse.exception.DuplicateTrackedKeywordException;
import ee.nikolas.marketpulse.exception.GlobalExceptionHandler;
import ee.nikolas.marketpulse.exception.TrackedKeywordNotFoundException;
import ee.nikolas.marketpulse.service.TrackedKeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrackedKeywordControllerTest {

    private MockMvc mockMvc;

    private TrackedKeywordService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        service = mock(TrackedKeywordService.class);

        TrackedKeywordController controller =
                new TrackedKeywordController(service);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(
                        new GlobalExceptionHandler()
                )
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateTrackedKeyword() throws Exception {

        TrackedKeywordRequestDto request =
                new TrackedKeywordRequestDto(
                        "wireless headphones",
                        Marketplace.EBAY,
                        20
                );

        TrackedKeywordResponseDto response =
                new TrackedKeywordResponseDto(
                        1L,
                        "wireless headphones",
                        Marketplace.EBAY,
                        true,
                        20,
                        null
                );

        when(service.create(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/tracked-keywords")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id").value(1)
                )
                .andExpect(
                        jsonPath("$.keyword")
                                .value("wireless headphones")
                )
                .andExpect(
                        jsonPath("$.marketplace")
                                .value("EBAY")
                )
                .andExpect(
                        jsonPath("$.active").value(true)
                )
                .andExpect(
                        jsonPath("$.searchLimit").value(20)
                );

        verify(service).create(any());
    }

    @Test
    void shouldReturnBadRequestForBlankKeyword()
            throws Exception {

        String body = """
                {
                    "keyword": "",
                    "marketplace": "EBAY",
                    "searchLimit": 20
                }
                """;

        mockMvc.perform(
                        post("/api/tracked-keywords")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(body)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.status").value(400)
                )
                .andExpect(
                        jsonPath("$.error")
                                .value("Bad Request")
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        org.hamcrest.Matchers
                                                .containsString(
                                                        "Keyword must not be blank"
                                                )
                                )
                );

        verifyNoInteractions(service);
    }

    @Test
    void shouldReturnBadRequestWhenSearchLimitTooHigh()
            throws Exception {

        String body = """
                {
                    "keyword": "gaming mouse",
                    "marketplace": "EBAY",
                    "searchLimit": 101
                }
                """;

        mockMvc.perform(
                        post("/api/tracked-keywords")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(body)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        org.hamcrest.Matchers
                                                .containsString(
                                                        "Search limit must not exceed 100"
                                                )
                                )
                );

        verifyNoInteractions(service);
    }

    @Test
    void shouldReturnConflictForDuplicateKeyword()
            throws Exception {

        when(service.create(any()))
                .thenThrow(
                        new DuplicateTrackedKeywordException(
                                "wireless headphones"
                        )
                );

        String body = """
                {
                    "keyword": "wireless headphones",
                    "marketplace": "EBAY",
                    "searchLimit": 20
                }
                """;

        mockMvc.perform(
                        post("/api/tracked-keywords")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(body)
                )
                .andExpect(
                        status().isConflict()
                )
                .andExpect(
                        jsonPath("$.status").value(409)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Keyword is already tracked: wireless headphones"
                                )
                );
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownKeyword()
            throws Exception {

        doThrow(
                new TrackedKeywordNotFoundException(999L)
        )
                .when(service)
                .delete(999L);

        mockMvc.perform(
                        delete(
                                "/api/tracked-keywords/999"
                        )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.status").value(404)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Tracked keyword not found: 999"
                                )
                );
    }

    @Test
    void shouldDeleteTrackedKeyword()
            throws Exception {

        mockMvc.perform(
                        delete(
                                "/api/tracked-keywords/1"
                        )
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(service).delete(1L);
    }
}