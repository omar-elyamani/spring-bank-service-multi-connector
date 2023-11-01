package ma.formations.graphql.dao;

import ma.formations.graphql.service.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByRib(String rib);
}
