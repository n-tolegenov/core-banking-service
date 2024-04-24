package kz.almaty.finance.corebankingservice.model.entity;

import jakarta.persistence.*;
import kz.almaty.finance.corebankingservice.model.AccountStatus;
import kz.almaty.finance.corebankingservice.model.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "banking_core_account")
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    /*
        Аннотация @Enumerated(EnumType.STRING) указывает JPA,
        что значения этого перечисления должны храниться в базе данных в виде строковых представлений
     */
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private BigDecimal availableBalance;

    private BigDecimal actualBalance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
