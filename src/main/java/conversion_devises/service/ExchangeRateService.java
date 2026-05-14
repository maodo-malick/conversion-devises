package conversion_devises.service;

import conversion_devises.dto.response.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final WebClient webClient;

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.url}")
    private String apiUrl;

    public ExchangeRateResponse getRates(String baseCurrency) {
        return webClient.get()
                .uri(apiUrl + "/" + apiKey + "/latest/" + baseCurrency)
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .block();
    }
}