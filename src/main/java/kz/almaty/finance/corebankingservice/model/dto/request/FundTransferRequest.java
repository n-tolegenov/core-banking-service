package kz.almaty.finance.corebankingservice.model.dto.request;

import lombok.Data;

@Data
public class FundTransferRequest {
    private String fromAccount;
    private String toAccount;
    private String amount;
}
