package conversion_devises.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private String sourceCurrency;
    private String targetCurrency;
    private Double amount;
    private Double convertedAmount;
    private Double rate;
    private LocalDateTime date;
}