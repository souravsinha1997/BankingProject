package com.banking.transaction_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_no", nullable = false, unique = true, length = 50)
    private String referenceNo;

    @Column(nullable = false, length = 20)
    private String cif;

    @Column(name = "user_account", nullable = false, length = 20)
    private String userAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "bnf_account", length = 20)
    private String beneficiaryAccount;

    @Column(name = "bnf_ifsc", length = 11)
    private String beneficiaryIfsc;

    @Column(name = "bnf_bank", length = 100)
    private String beneficiaryBank;

    @Column(name = "txn_type", nullable = false, length = 50)
    //@Enumerated(EnumType.STRING)
    private String transactionType;

    @Column(name = "txn_status", length = 20)
    private String transactionStatus = "pending";

    @Column(name = "txn_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime transactionDate;
}
