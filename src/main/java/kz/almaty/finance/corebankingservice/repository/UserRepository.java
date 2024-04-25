package kz.almaty.finance.corebankingservice.repository;

import kz.almaty.finance.corebankingservice.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByIdentificationNumber(String identificationNUmber);
}
