package ee.nikolas.marketpulse.exception;

public class TrackedKeywordNotFoundException extends RuntimeException {

    public TrackedKeywordNotFoundException(Long id) {
        super("Tracked keyword not found: " + id);
    }
}
