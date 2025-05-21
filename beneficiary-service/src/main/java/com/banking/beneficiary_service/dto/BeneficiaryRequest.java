package com.banking.beneficiary_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryRequest {
    private String name;
    private String fullName;
    private String bankName;
    private String accountNo;
    private String ifscCode;
}
