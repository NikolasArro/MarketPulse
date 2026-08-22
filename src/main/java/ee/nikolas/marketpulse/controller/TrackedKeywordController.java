package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordSummaryDto;
import ee.nikolas.marketpulse.service.TrackedKeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/tracked-keywords")
@RequiredArgsConstructor
@Tag(
        name = "Tracked Keywords",
        description = "Manage keywords used for automatic product monitoring"
)
public class TrackedKeywordController {

    private final TrackedKeywordService service;

    @Operation(summary = "Get all tracked keywords")
    @GetMapping
    public List<TrackedKeywordResponseDto> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Add a keyword for monitoring")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackedKeywordResponseDto create(
            @Valid @RequestBody TrackedKeywordRequestDto request
    ) {
        return service.create(request);
    }

    @Operation(summary = "Enable or disable keyword monitoring")
    @PatchMapping("/{id}/active")
    public TrackedKeywordResponseDto setActive(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return service.setActive(id, active);
    }

    @Operation(summary = "Delete a tracked keyword")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        service.delete(id);
    }

    @Operation(summary = "Get analytics summary for a tracked keyword")
    @GetMapping("/{id}/summary")
    public TrackedKeywordSummaryDto getSummary(
            @PathVariable Long id
    ) {
        return service.getSummary(id);
    }
}