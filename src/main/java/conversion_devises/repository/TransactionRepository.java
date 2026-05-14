package conversion_devises.repository;
import conversion_devises.entity.Transaction;
import conversion_devises.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long id);
    List<Transaction> findByUser(User user);
}
