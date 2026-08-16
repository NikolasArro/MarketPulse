package ee.nikolas.marketpulse.controller;

import ee.nikolas.marketpulse.dto.TrackedKeywordRequestDto;
import ee.nikolas.marketpulse.dto.TrackedKeywordResponseDto;
import ee.nikolas.marketpulse.service.TrackedKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracked-keywords")
@RequiredArgsConstructor
public class TrackedKeywordController {

    private final TrackedKeywordService service;

    @GetMapping
    public List<TrackedKeywordResponseDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackedKeywordResponseDto create(
            @RequestBody TrackedKeywordRequestDto request
    ) {
        return service.create(request);
    }

    @PatchMapping("/{id}/active")
    public TrackedKeywordResponseDto setActive(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return service.setActive(id, active);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        service.delete(id);
    }
}