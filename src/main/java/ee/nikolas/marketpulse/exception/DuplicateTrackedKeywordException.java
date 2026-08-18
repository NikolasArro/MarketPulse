package ee.nikolas.marketpulse.exception;

public class DuplicateTrackedKeywordException extends RuntimeException {

    public DuplicateTrackedKeywordException(String keyword) {
        super("Keyword is already tracked: " + keyword);
    }
}
