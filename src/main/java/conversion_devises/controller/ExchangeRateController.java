package conversion_devises.controller;

import conversion_devises.dto.response.ExchangeRateResponse;
import conversion_devises.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<ExchangeRateResponse> getRates(
            @RequestParam(defaultValue = "USD") String base) {
        return ResponseEntity.ok(exchangeRateService.getRates(base));
    }
}