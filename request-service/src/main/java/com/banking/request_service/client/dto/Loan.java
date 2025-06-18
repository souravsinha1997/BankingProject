package com.banking.request_service.client.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    private Integer id;
    private String cif;
    private String accountNo;
    private BigDecimal amount;
    private String status;
    private Integer reqId;
    private LocalDateTime startDate;
}
