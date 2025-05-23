package com.banking.account_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Account findByAccountNumber(String accountNo);
	List<Account> findByCif(String cif);

}
