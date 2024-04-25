package kz.almaty.finance.corebankingservice.model.mapper;

import kz.almaty.finance.corebankingservice.model.dto.BankAccount;
import kz.almaty.finance.corebankingservice.model.entity.BankAccountEntity;
import org.springframework.beans.BeanUtils;

public class BankAccountMapper extends BaseMapper<BankAccountEntity, BankAccount>{
    @Override
    public BankAccountEntity convertToEntity(BankAccount dto, Object... args) {
        BankAccountEntity entity = new BankAccountEntity();
        if(dto != null){
            /*
                BeanUtils может копировать свойства из одного JavaBean в другой.
                Это удобно, например,
                при маппинге данных из DTO (объекты для передачи данных) в объекты модели или наоборот.
             */
            BeanUtils.copyProperties(dto, entity, "user");
        }
        return entity;
    }

    @Override
    public BankAccount convertToDto(BankAccountEntity entity, Object... args) {
        BankAccount dto = new BankAccount();
        if(entity != null){
            BeanUtils.copyProperties(entity, dto, "user");
        }
        return dto;
    }
}
