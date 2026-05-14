package conversion_devises.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Builder
@Data
public class ConversionResponse {
    private String sourceCurrency;
    private String targetCurrency;
    private double amount;
    private double convertedAmount;
    private  double rate;
    private LocalDateTime date;
}
