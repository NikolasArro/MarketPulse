package ee.nikolas.marketpulse.controller.ebay;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/ebay/notifications")
@RequiredArgsConstructor
public class EbayNotificationController {

    @Value("${ebay.notification.verification-token}")
    private String verificationToken;

    @Value("${ebay.notification.endpoint}")
    private String endpoint;

    @GetMapping
    public ResponseEntity<Map<String, String>> verifyEndpoint(
            @RequestParam("challenge_code") String challengeCode
    ) throws NoSuchAlgorithmException {

        String valueToHash =
                challengeCode +
                        verificationToken +
                        endpoint;

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] hash = digest.digest(
                valueToHash.getBytes(StandardCharsets.UTF_8)
        );

        String challengeResponse =
                HexFormat.of().formatHex(hash);

        return ResponseEntity.ok(
                Map.of("challengeResponse", challengeResponse)
        );
    }

    @PostMapping
    public ResponseEntity<Void> receiveNotification(
            @RequestBody String body
    ) {
        System.out.println("eBay notification: " + body);

        return ResponseEntity.noContent().build();
    }
}