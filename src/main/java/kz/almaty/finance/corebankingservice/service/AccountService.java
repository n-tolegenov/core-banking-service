package kz.almaty.finance.corebankingservice.service;

import kz.almaty.finance.corebankingservice.model.dto.BankAccount;
import kz.almaty.finance.corebankingservice.model.dto.UtilityAccount;
import kz.almaty.finance.corebankingservice.model.entity.BankAccountEntity;
import kz.almaty.finance.corebankingservice.model.entity.UtilityAccountEntity;
import kz.almaty.finance.corebankingservice.model.mapper.BankAccountMapper;
import kz.almaty.finance.corebankingservice.model.mapper.UtilityAccountMapper;
import kz.almaty.finance.corebankingservice.repository.BankAccountRepository;
import kz.almaty.finance.corebankingservice.repository.UtilityAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private BankAccountMapper bankAccountMapper = new BankAccountMapper();
    private UtilityAccountMapper utilityAccountMapper = new UtilityAccountMapper();

    private final BankAccountRepository bankAccountRepository;
    private final UtilityAccountRepository utilityAccountRepository;

    public BankAccount readBankAccount(String accountNumber){
        BankAccountEntity entity = bankAccountRepository.findByNumber(accountNumber).get();
        return bankAccountMapper.convertToDto(entity);
    }

    public UtilityAccount readUtilityAccount(String provider){
        UtilityAccountEntity utilityAccountEntity = utilityAccountRepository.findByProviderName(provider).get();
        return utilityAccountMapper.convertToDto(utilityAccountEntity);
    }

    public UtilityAccount readUtilityAccount(Long id){
        return utilityAccountMapper.convertToDto(utilityAccountRepository.findById(id).get());
    }
}
