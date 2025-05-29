package com.banking.account_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.banking.account_service.client.UserClient;
import com.banking.account_service.client.dto.AccountRequest;
import com.banking.account_service.client.dto.AccountResponse;
import com.banking.account_service.client.dto.BankTransferRequest;
import com.banking.account_service.client.dto.Status;
import com.banking.account_service.client.dto.TransactionRequest;
import com.banking.account_service.client.dto.UserResponse;
import com.banking.account_service.entity.Account;
import com.banking.account_service.entity.AccountStatus;
import com.banking.account_service.exception.InsufficientBalanceException;
import com.banking.account_service.exception.InvalidCifException;
import com.banking.account_service.exception.NoAccountFoundException;
import com.banking.account_service.exception.UnableToCompleteException;
import com.banking.account_service.rabbitMQ.StatusPublisher;
import com.banking.account_service.repository.AccountRepository;
import com.banking.account_service.security.JwtService;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private StatusPublisher statusPublisher;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getCredentials().toString();
        return Long.valueOf(jwtService.getCustomerId(token));
    }

    private String getCurrentCif() {
        int userId = getCurrentUserId().intValue();
        UserResponse user = userClient.getUser(userId).getBody();
        return user.getCif();
    }

    private void validateCif(String actualCif) {
        String currentCif = getCurrentCif();
        if (!actualCif.equals(currentCif)) {
            throw new InvalidCifException("Invalid CIF for the account");
        }
    }

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        String cif = getCurrentCif();

        Account account = new Account();
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance() != null ? accountRequest.getBalance() : BigDecimal.ZERO);
        account.setCurrency(accountRequest.getCurrency());
        account.setCif(cif);

        account.setAccountNumber(accountRequest.getAccountNumber() != null
                ? accountRequest.getAccountNumber()
                : generateAccountNumber());

        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);
        return convertToResponse(savedAccount);
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        String cif = getCurrentCif();
        return accountRepository.findByCif(cif)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public AccountResponse getAccountByAccountNo(String accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo);
        if (account == null) {
            throw new NoAccountFoundException("No account found with account no: " + accountNo);
        }
        validateCif(account.getCif());
        return convertToResponse(account);
    }
    
    @Override
    public String getCIFByAccountNo(String accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo);
        if (account == null) {
            throw new NoAccountFoundException("No account found with account no: " + accountNo);
        }
        //validateCif(account.getCif());
        return account.getCif();
    }
    
    public boolean validateAccount(String accountNo) {
    	Account account = accountRepository.findByAccountNumber(accountNo);
    	if(account!=null) return true;
    	else return false;
    }

    @Override
    public AccountResponse updateAccount(AccountRequest accountDetails) {
        Account account = accountRepository.findByAccountNumber(accountDetails.getAccountNumber());
        validateCif(account.getCif());

        account.setAccountType(accountDetails.getAccountType());
        account.setBalance(accountDetails.getBalance() != null ? accountDetails.getBalance() : account.getBalance());
        account.setCurrency(accountDetails.getCurrency() != null ? accountDetails.getCurrency() : account.getCurrency());

        Account updatedAccount = accountRepository.save(account);
        return convertToResponse(updatedAccount);
    }

    @Override
    public AccountResponse withdrawOrDepositFunds(TransactionRequest request) {
        try{
        	Account account = accountRepository.findByAccountNumber(request.getAccountNo());

        	if (account == null) {
        		throw new NoAccountFoundException("Invalid account number");
        	}
        

        	BigDecimal amount = request.getAmount();
        	switch (request.getTransaction()) {
            	case "DEPOSIT" -> account.setBalance(account.getBalance().add(amount));
            	case "WITHDRAW" -> {
            		if (account.getBalance().compareTo(amount) < 0) {
            			throw new InsufficientBalanceException("Insufficient balance in account");
            		}
            		account.setBalance(account.getBalance().subtract(amount));
            	}
            	default -> throw new IllegalArgumentException("Invalid transaction type");
        	}

        	Account updatedAccount = accountRepository.save(account);
        	
        	publishStatus(request.getTxnRefNo(),"SUC");
        	return convertToResponse(updatedAccount);
        } catch (NoAccountFoundException | InsufficientBalanceException | IllegalArgumentException e) {
            publishStatus(request.getTxnRefNo(), "FAL");
            throw e;
        } catch (Exception e) {
            publishStatus(request.getTxnRefNo(), "FAL");
            throw new UnableToCompleteException("Unable to complete transaction",e);
        }
        
    }
    
    private void publishStatus(String txnRefNo, String statusCode) {
        Status status = new Status(txnRefNo, statusCode);
        statusPublisher.sendTransactionQueue(status);
    }
    
    @Override
    @Transactional
    public void transferFunds(BankTransferRequest request) {
        try {
            Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccount());
            Account toAccount = accountRepository.findByAccountNumber(request.getToAccount());

            if (fromAccount == null || toAccount == null) {
                throw new NoAccountFoundException("Invalid source or destination account");
            }

            BigDecimal transferAmount = request.getAmmount();
            BigDecimal fromBalance = fromAccount.getBalance();

            if (fromBalance.compareTo(transferAmount) < 0) {
                throw new InsufficientBalanceException("Insufficient balance in account");
            }

            fromAccount.setBalance(fromBalance.subtract(transferAmount));
            toAccount.setBalance(toAccount.getBalance().add(transferAmount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            publishStatus(request.getTxnRefNo(), "SUC");

        } catch (NoAccountFoundException | InsufficientBalanceException e) {
            publishStatus(request.getTxnRefNo(), "FAL");
            throw e;
        } catch (Exception e) {
            publishStatus(request.getTxnRefNo(), "FAL");
            throw new UnableToCompleteException("Transfer failed", e);
        }
    }
    
    

    private AccountResponse convertToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setCif(account.getCif());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setStatus(account.getStatus());
        return response;
    }

    @Override
    public String closeAccount(String accountNo) {
        Account account = accountRepository.findByAccountNumber(accountNo);
        if (account == null) {
            throw new NoAccountFoundException("No account found with account no: " + accountNo);
        }
        validateCif(account.getCif());

        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
        return "Account closed successfully";
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }
}

