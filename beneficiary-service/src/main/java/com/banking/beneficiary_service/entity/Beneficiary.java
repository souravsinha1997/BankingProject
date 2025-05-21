package com.banking.beneficiary_service.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beneficiary")
public class Beneficiary implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 150)
    private String fullName;
    
    @Column(nullable = false, length = 100)
    private String bankName;
    
    @Column(nullable = false, length = 30)
    private String accountNo;
    
    @Column(nullable = false, length = 20)
    private String ifscCode;
    
    @Column(nullable = false)
    private Long userId;
}

