package ee.nikolas.marketpulse.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI marketPulseOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("MarketPulse API")
                                .version("1.0")
                                .description(
                                        "Market monitoring API for tracking eBay products, " +
                                                "search positions, popularity and product trends."
                                )
                );
    }
}