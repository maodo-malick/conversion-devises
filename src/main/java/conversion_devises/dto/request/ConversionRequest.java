package conversion_devises.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ConversionRequest {

    @NotBlank(message = "La devise source est obligatoire")
    private String sourceCurrency;

    @NotBlank(message = "La devise cible est obligatoire")
    private String targetCurrency;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double amount;
}