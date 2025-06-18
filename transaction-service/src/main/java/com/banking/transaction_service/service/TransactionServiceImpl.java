package com.banking.transaction_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.banking.transaction_service.client.AccountClient;
import com.banking.transaction_service.client.BeneficiaryClient;
import com.banking.transaction_service.client.UserClient;
import com.banking.transaction_service.client.dto.BankTransactionRequest;
import com.banking.transaction_service.client.dto.BankTransferRequest;
import com.banking.transaction_service.client.dto.BeneficiaryRequest;
import com.banking.transaction_service.client.dto.BeneficiaryResponse;
import com.banking.transaction_service.client.dto.StatementRequest;
import com.banking.transaction_service.client.dto.StatementTransaction;
import com.banking.transaction_service.client.dto.Status;
import com.banking.transaction_service.client.dto.UserResponse;
import com.banking.transaction_service.dto.TransactionRequest;
import com.banking.transaction_service.entity.TransactionType;
import com.banking.transaction_service.entity.Transactions;
import com.banking.transaction_service.exception.InvalidCifException;
import com.banking.transaction_service.exception.UnableToUpdateTransaction;
import com.banking.transaction_service.rabbitMQ.BankTransferAndTransactionPublisher;
import com.banking.transaction_service.repository.TransactionRepository;
import com.banking.transaction_service.security.JwtService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private BeneficiaryClient beneficiaryClient;
	
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	
	@Autowired
	private BankTransferAndTransactionPublisher publisher;
	
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
	@Transactional
	public String doTransaction(TransactionRequest request) {
		validateCif(request.getCif());
		
		String fromAccount = request.getAccountNo();
		String cif = request.getCif();
		BigDecimal ammount = request.getAmmount();
		
		int bnf_id = request.getBnf_id();
		
		
		String bnf_name = request.getBnf_name();
		String bnf_fullName = request.getBnf_fullName();
		String bnf_account = request.getBnf_accountNo();
		String bnf_bank = request.getBnf_bank();
		String bnf_ifsc = request.getBnf_ifsc();
		
		
		TransactionType txn_type = request.getTransactionType();
		
		
		String txnRefNo = generateTransactionReferenceNo();
		
		if(bnf_id==0) {
			if(bnf_account==null) {
				if(txn_type.equals(TransactionType.DEPOSIT)|| txn_type.equals(TransactionType.WITHDRAW) || txn_type.equals(TransactionType.LOAN)) {
					System.out.println(txn_type);
					BankTransactionRequest transactionRequest = new BankTransactionRequest();
					transactionRequest.setAccountNo(fromAccount);
					transactionRequest.setAmount(ammount);
					transactionRequest.setTransaction(txn_type.toString());
					transactionRequest.setTxnRefNo(txnRefNo);
				
					//Transaction table entry with pending status
					Transactions transactions = createTransactionsObject(fromAccount,null,ammount,txnRefNo,txn_type.toString(),cif,null,null);
					transactionRepo.save(transactions);
					
					
					//** RabbitMQ call for transaction
					publisher.sendTransactionQueue(transactionRequest);
					
				}
			}
			else if(bnf_account!= null && txn_type.equals(TransactionType.OWN_BANK_ADHOC)) {
				BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
				beneficiaryRequest.setAccountNo(bnf_account);
				beneficiaryRequest.setBankName(bnf_bank);
				beneficiaryRequest.setFullName(bnf_fullName);
				beneficiaryRequest.setName(bnf_name);
				beneficiaryRequest.setIfscCode(bnf_ifsc);
				
				beneficiaryClient.addBeneficiary(beneficiaryRequest);
				
				BankTransferRequest bankTransfer = new BankTransferRequest();
				bankTransfer.setFromAccount(fromAccount);
				bankTransfer.setToAccount(bnf_account);
				bankTransfer.setAmmount(ammount);
				bankTransfer.setTxnRefNo(txnRefNo);
				
				//**Transaction Table entry with pending status
				Transactions debitTransactions = createTransactionsObject(fromAccount,bnf_account,ammount,txnRefNo,txn_type.toString()+" - Debit",cif,bnf_bank,bnf_ifsc);
				
				
				String bnf_cif = accountClient.getAccountCIF(bnf_account).getBody();
				
				Transactions creditTransactions = createTransactionsObject(bnf_account,fromAccount,ammount,txnRefNo,txn_type.toString()+" - Credit",bnf_cif,null,null);
				
				transactionRepo.save(debitTransactions);
				transactionRepo.save(creditTransactions);
				
				//** RabbitMQ call for bank Transfer
				publisher.sendTransferQueue(bankTransfer);
			}
		}
		else if(Integer.valueOf(bnf_id)!=null && txn_type.equals(TransactionType.OWN_BANK_BNF)) {
			BeneficiaryResponse beneficiary = beneficiaryClient.getBeneficiary(Long.valueOf(bnf_id)).getBody();
			bnf_name = beneficiary.getName();
			bnf_fullName = beneficiary.getFullName();
			bnf_account = beneficiary.getAccountNo();
			bnf_bank = beneficiary.getBankName();
			bnf_ifsc = beneficiary.getIfscCode();
			
			BankTransferRequest bankTransfer = new BankTransferRequest();
			bankTransfer.setFromAccount(fromAccount);
			bankTransfer.setToAccount(bnf_account);
			bankTransfer.setAmmount(ammount);
			bankTransfer.setTxnRefNo(txnRefNo);
			//**Transaction Table entry with pending status
			Transactions debitTransactions = createTransactionsObject(fromAccount,bnf_account,ammount,txnRefNo,txn_type.toString()+" - Debit",cif,bnf_bank,bnf_ifsc);
			
			
			String bnf_cif = accountClient.getAccountCIF(bnf_account).getBody();
			
			Transactions creditTransactions = createTransactionsObject(bnf_account,fromAccount,ammount,txnRefNo,txn_type.toString()+" - Credit",bnf_cif,null,null);
			
			transactionRepo.save(debitTransactions);
			transactionRepo.save(creditTransactions);
			
			//**RabbitMQ call for the bank transfer
			publisher.sendTransferQueue(bankTransfer);
		}
		
		return "Transaction initiated successfully with reference no : "+txnRefNo;
	}
	
	
	
	@Override
	@Transactional
	public String doLoanTransaction(TransactionRequest request) {
		
		String fromAccount = request.getAccountNo();
		String cif = request.getCif();
		BigDecimal ammount = request.getAmmount();
		
		int bnf_id = request.getBnf_id();
		
		
		TransactionType txn_type = request.getTransactionType();
		
		
		String txnRefNo = generateTransactionReferenceNo();
		
		if(bnf_id==0) {
				if(txn_type.equals(TransactionType.LOAN)) {
					System.out.println(txn_type);
					BankTransactionRequest transactionRequest = new BankTransactionRequest();
					transactionRequest.setAccountNo(fromAccount);
					transactionRequest.setAmount(ammount);
					transactionRequest.setTransaction(txn_type.toString());
					transactionRequest.setTxnRefNo(txnRefNo);
				
					//Transaction table entry with pending status
					Transactions transactions = createTransactionsObject(fromAccount,null,ammount,txnRefNo,txn_type.toString(),cif,null,null);
					transactionRepo.save(transactions);
					
					
					//** RabbitMQ call for transaction
					publisher.sendTransactionQueue(transactionRequest);
					
				}
			}
		
		
		return "Transaction initiated successfully with reference no : "+txnRefNo;
	}
	
	public Transactions createTransactionsObject(String from,String to,BigDecimal amount,String txnRefNo,String txnType,String cif,String bnf_bank,String bnf_ifsc) {
		Transactions transaction = new Transactions();
		transaction.setAmount(amount);
		transaction.setBeneficiaryAccount(to);
		transaction.setUserAccount(from);
		transaction.setBeneficiaryBank(bnf_bank);
		transaction.setBeneficiaryIfsc(bnf_ifsc);
		transaction.setCif(cif);
		transaction.setReferenceNo(txnRefNo);
		transaction.setTransactionType(txnType);
		transaction.setTransactionStatus("EIP");
		transaction.setTransactionDate(LocalDateTime.now());
		
		return transaction;
		
	}
	
	@Transactional
	public void updateTransaction(Status status) {
		try {
			List<Transactions> transactions = transactionRepo.findByReferenceNo(status.getTxnRefNo());
			List<Transactions> updatedTransactions = transactions.stream()
				    .map(t -> {
				        t.setTransactionStatus(status.getStatus());
				        return t;
				    })
				    .toList();

			transactionRepo.saveAll(updatedTransactions);
			
		} catch (Exception e) {
			throw new UnableToUpdateTransaction("Unable to update the transaction table");
		}
	}
	
	public static String generateTransactionReferenceNo() {
        String prefix = "TXN";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "-" + timestamp + "-" + randomSuffix;
    }

	@Override
	public List<StatementTransaction> getAccountStatement(String accountNo) {
		String cif = getCurrentCif();
		List<Transactions> transactions = transactionRepo.findByCifAndUserAccount(cif, accountNo);
		List<StatementTransaction> statement = transactions.stream().map(t -> {
																			StatementTransaction statementTransaction = new StatementTransaction();
																			statementTransaction.setAccoutNo(t.getUserAccount());
																			statementTransaction.setAmount(t.getAmount());
																			statementTransaction.setBnfAccount(t.getBeneficiaryAccount());
																			statementTransaction.setTxnDate(t.getTransactionDate());
																			statementTransaction.setTxnRefNo(t.getReferenceNo());
																			statementTransaction.setTxnType(t.getTransactionType());
																			return statementTransaction;
																			}).toList();
		return statement;
	}


}
