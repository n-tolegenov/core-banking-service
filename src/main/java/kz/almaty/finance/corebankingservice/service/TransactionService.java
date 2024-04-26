package kz.almaty.finance.corebankingservice.service;

import kz.almaty.finance.corebankingservice.model.TransactionType;
import kz.almaty.finance.corebankingservice.model.dto.BankAccount;
import kz.almaty.finance.corebankingservice.model.dto.UtilityAccount;
import kz.almaty.finance.corebankingservice.model.dto.request.FundTransferRequest;
import kz.almaty.finance.corebankingservice.model.dto.request.UtilityPaymentRequest;
import kz.almaty.finance.corebankingservice.model.dto.response.FundTransferResponse;
import kz.almaty.finance.corebankingservice.model.dto.response.UtilityPaymentResponse;
import kz.almaty.finance.corebankingservice.model.entity.BankAccountEntity;
import kz.almaty.finance.corebankingservice.model.entity.TransactionEntity;
import kz.almaty.finance.corebankingservice.repository.BankAccountRepository;
import kz.almaty.finance.corebankingservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
/*
    @Transactional - это аннотация в Spring Framework,
    которая используется для указания транзакционного поведения методов или классов.
    Она позволяет управлять транзакциями в приложении,
    гарантируя атомарность, согласованность, изолированность и долговечность операций с данными.
 */
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final AccountService accountService;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest){
        BankAccount fromBankAccount = accountService.readBankAccount(fundTransferRequest.getFromAccount());
        BankAccount toBankAccount = accountService.readBankAccount(fundTransferRequest.getToAccount());

        validateBalance(fromBankAccount, fundTransferRequest.getAmount());

        String transactionId = internalFundTransfer(fromBankAccount, toBankAccount, fundTransferRequest.getAmount());
        return FundTransferResponse.builder()
                .message("Transaction successfully completed")
                .transactionId(transactionId)
                .build();
    }

    public UtilityPaymentResponse utilPayment(UtilityPaymentRequest utilityPaymentRequest){
        String transactionId = UUID.randomUUID().toString();
        BankAccount fromBankAccount = accountService.readBankAccount(utilityPaymentRequest.getAccount());

        validateBalance(fromBankAccount, utilityPaymentRequest.getAmount());

        UtilityAccount utilityAccount = accountService.readUtilityAccount(utilityPaymentRequest.getProviderId());

        BankAccountEntity fromAccount = bankAccountRepository.findByNumber(fromBankAccount.getNumber()).get();

        fromAccount.setActualBalance(fromAccount.getActualBalance().subtract(utilityPaymentRequest.getAmount()));
        fromAccount.setAvailableBalance(fromAccount.getActualBalance().subtract(utilityPaymentRequest.getAmount()));

        transactionRepository.save(TransactionEntity.builder().transactionType(TransactionType.UTILITY_PAYMENT)
                .account(fromAccount)
                .transactionId(transactionId)
                .referenceNumber(utilityPaymentRequest.getReferenceNumber())
                .amount(utilityPaymentRequest.getAmount().negate()).build());

        return UtilityPaymentResponse.builder()
                .message("Utility payment successfully completed")
                .transactionId(transactionId)
                .build();
    }

    private String internalFundTransfer(BankAccount fromBankAccount, BankAccount toBankAccount, BigDecimal amount) {

        // UUID: Для генерации уникальных идентификаторов транзакций.
        String transactionId = UUID.randomUUID().toString();

        BankAccountEntity fromBankAccountEntity = bankAccountRepository.findByNumber(fromBankAccount.getNumber()).get();
        BankAccountEntity toBankAccountEntity = bankAccountRepository.findByNumber(toBankAccount.getNumber()).get();

        /*
            Метод subtract() принимает в качестве аргумента другой объект BigDecimal
            и выполняет операцию вычитания текущего значения объекта BigDecimal на который он вызван,
            из переданного аргумента. Результатом является новый объект BigDecimal,
            содержащий разницу между этими значениями.
         */
        fromBankAccountEntity.setActualBalance(fromBankAccountEntity.getActualBalance().subtract(amount));
        fromBankAccountEntity.setAvailableBalance(fromBankAccountEntity.getActualBalance().subtract(amount));
        bankAccountRepository.save(fromBankAccountEntity);

        transactionRepository.save(TransactionEntity
                .builder()
                .transactionType(TransactionType.FUND_TRANSFER)
                .transactionId(transactionId)
                .account(fromBankAccountEntity)
                .amount(amount.negate())
                .build());

        toBankAccountEntity.setActualBalance(fromBankAccountEntity.getActualBalance().add(amount));
        toBankAccountEntity.setAvailableBalance(toBankAccountEntity.getActualBalance().add(amount));
        bankAccountRepository.save(toBankAccountEntity);

        transactionRepository.save(TransactionEntity
                .builder()
                .transactionType(TransactionType.FUND_TRANSFER)
                .transactionId(transactionId)
                .account(toBankAccountEntity)
                .amount(amount)
                .build());

        return transactionId;
    }

    private void validateBalance(BankAccount bankAccount, BigDecimal amount) {
        if(bankAccount.getActualBalance().compareTo(BigDecimal.ZERO) < 0 || bankAccount.getAvailableBalance().compareTo(amount) < 0){
            throw new RuntimeException();
        }
    }
}
