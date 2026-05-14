package conversion_devises.service;

import conversion_devises.dto.response.TransactionResponse;
import conversion_devises.entity.User;
import conversion_devises.repository.TransactionRepository;
import conversion_devises.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<TransactionResponse> getHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return transactionRepository.findByUser(user)
                .stream()
                .map(transaction -> TransactionResponse.builder()
                        .id(transaction.getId())
                        .sourceCurrency(transaction.getSourceCurrency())
                        .targetCurrency(transaction.getTargetCurrency())
                        .amount(transaction.getAmount())
                        .convertedAmount(transaction.getConvertedAmount())
                        .rate(transaction.getRate())
                        .date(transaction.getDate())
                        .build())
                .collect(Collectors.toList());
    }
}