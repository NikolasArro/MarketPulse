package ee.nikolas.marketpulse.client.ebay;

import ee.nikolas.marketpulse.client.ebay.dto.EbayAccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EbayAuthService {

    private static final String TOKEN_URL =
            "https://api.ebay.com/identity/v1/oauth2/token";

    private final RestTemplate restTemplate;

    @Value("${ebay.client-id}")
    private String clientId;

    @Value("${ebay.client-secret}")
    private String clientSecret;

    private String accessToken;
    private Instant expiresAt;

    public synchronized String getAccessToken() {

        if (accessToken != null
                && expiresAt != null
                && Instant.now().isBefore(expiresAt.minusSeconds(60))) {

            return accessToken;
        }

        return requestNewToken();
    }

    private String requestNewToken() {

        String credentials = clientId + ":" + clientSecret;

        String encodedCredentials = Base64.getEncoder()
                .encodeToString(
                        credentials.getBytes(StandardCharsets.UTF_8)
                );

        HttpHeaders headers = new HttpHeaders();

        headers.set(
                HttpHeaders.AUTHORIZATION,
                "Basic " + encodedCredentials
        );

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add(
                "grant_type",
                "client_credentials"
        );

        body.add(
                "scope",
                "https://api.ebay.com/oauth/api_scope"
        );

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<EbayAccessTokenResponse> response =
                restTemplate.exchange(
                        TOKEN_URL,
                        HttpMethod.POST,
                        request,
                        EbayAccessTokenResponse.class
                );

        EbayAccessTokenResponse tokenResponse =
                response.getBody();

        if (tokenResponse == null) {
            throw new IllegalStateException(
                    "eBay OAuth returned empty response"
            );
        }

        accessToken = tokenResponse.accessToken();

        expiresAt = Instant.now()
                .plusSeconds(tokenResponse.expiresIn());

        return accessToken;
    }
}