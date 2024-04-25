package kz.almaty.finance.corebankingservice.repository;

import kz.almaty.finance.corebankingservice.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
