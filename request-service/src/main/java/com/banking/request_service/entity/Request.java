package com.banking.request_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests", indexes = {
    @Index(name = "idx_cif", columnList = "cif"),
    @Index(name = "idx_account_no", columnList = "account_no"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String cif;

    @Column(name = "account_no", length = 30, nullable = false)
    private String accountNo;

    @Column(length = 50, nullable = false)
    private RequestType type;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(name = "initiation_date", nullable = false, updatable = false)
    private LocalDateTime initiationDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    private Integer approver;

    @PrePersist
    protected void onCreate() {
        this.initiationDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
