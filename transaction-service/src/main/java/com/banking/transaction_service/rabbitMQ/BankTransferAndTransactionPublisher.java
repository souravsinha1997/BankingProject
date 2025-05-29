package com.banking.transaction_service.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.transaction_service.client.dto.BankTransactionRequest;
import com.banking.transaction_service.client.dto.BankTransferRequest;
import com.banking.transaction_service.config.RabbitMQConfig;

@Service
public class BankTransferAndTransactionPublisher {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	public void sendTransactionQueue(BankTransactionRequest transaction) {
		amqpTemplate.convertAndSend(RabbitMQConfig.TRANSACTION_QUEUE,transaction);
		System.out.println("TXN initiated with reference no : "+transaction.getTxnRefNo());
	}
	
	public void sendTransferQueue(BankTransferRequest transfer) {
		amqpTemplate.convertAndSend(RabbitMQConfig.TRANSFER_QUEUE,transfer);
		System.out.println("TXN initiated with reference no : "+transfer.getTxnRefNo());
	}
}
