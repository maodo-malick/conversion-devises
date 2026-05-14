package conversion_devises.service;

import conversion_devises.dto.request.ConversionRequest;
import conversion_devises.dto.response.ConversionResponse;
import conversion_devises.entity.Transaction;
import conversion_devises.entity.User;
import conversion_devises.repository.TransactionRepository;
import conversion_devises.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConversionService {

    private final ExchangeRateService exchangeRateService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public ConversionResponse convert(ConversionRequest request, String email) {

        // 1. Récupérer l'utilisateur connecté
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 2. Récupérer les taux de change
        var rates = exchangeRateService.getRates(request.getSourceCurrency());

        // 3. Extraire le taux de la devise cible
        Double rate = rates.getConversionRates().get(request.getTargetCurrency());
        if (rate == null) {
            throw new RuntimeException("Devise cible introuvable : " + request.getTargetCurrency());
        }

        // 4. Calculer le montant converti
        Double convertedAmount = request.getAmount() * rate;

        // 5. Sauvegarder la transaction
        Transaction transaction = Transaction.builder()
                .user(user)
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .rate(rate)
                .date(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        // 6. Retourner la réponse
        return ConversionResponse.builder()
                .sourceCurrency(request.getSourceCurrency())
                .targetCurrency(request.getTargetCurrency())
                .amount(request.getAmount())
                .convertedAmount(convertedAmount)
                .rate(rate)
                .date(LocalDateTime.now())
                .build();
    }
}