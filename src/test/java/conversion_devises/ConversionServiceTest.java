package conversion_devises.service;

import conversion_devises.dto.request.ConversionRequest;
import conversion_devises.dto.response.ConversionResponse;
import conversion_devises.dto.response.ExchangeRateResponse;
import conversion_devises.entity.User;
import conversion_devises.repository.TransactionRepository;
import conversion_devises.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConversionService conversionService;

    @Test
    void convert_shouldReturnConvertedAmount_whenCurrencyIsValid() {
        ConversionRequest request = new ConversionRequest();
        request.setSourceCurrency("USD");
        request.setTargetCurrency("XOF");
        request.setAmount(100.0);

        User user = User.builder()
                .id(1L)
                .email("moussa@gmail.com")
                .build();

        ExchangeRateResponse ratesResponse = new ExchangeRateResponse();
        ratesResponse.setConversionRates(Map.of("XOF", 559.97));

        when(userRepository.findByEmail("moussa@gmail.com")).thenReturn(Optional.of(user));
        when(exchangeRateService.getRates("USD")).thenReturn(ratesResponse);
        when(transactionRepository.save(any())).thenReturn(null);

        ConversionResponse response = conversionService.convert(request, "moussa@gmail.com");

        assertNotNull(response);
        assertEquals(55997.0, response.getConvertedAmount(), 1.0);
        assertEquals("USD", response.getSourceCurrency());
        assertEquals("XOF", response.getTargetCurrency());
    }

    @Test
    void convert_shouldThrowException_whenTargetCurrencyNotFound() {
        ConversionRequest request = new ConversionRequest();
        request.setSourceCurrency("USD");
        request.setTargetCurrency("INVALID");
        request.setAmount(100.0);

        User user = User.builder()
                .id(1L)
                .email("moussa@gmail.com")
                .build();

        ExchangeRateResponse ratesResponse = new ExchangeRateResponse();
        ratesResponse.setConversionRates(Map.of("XOF", 559.97));

        when(userRepository.findByEmail("moussa@gmail.com")).thenReturn(Optional.of(user));
        when(exchangeRateService.getRates("USD")).thenReturn(ratesResponse);

        assertThrows(RuntimeException.class,
                () -> conversionService.convert(request, "moussa@gmail.com"));
    }
}