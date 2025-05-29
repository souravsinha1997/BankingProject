package com.banking.account_service.rabbitMQ;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.banking.account_service.client.dto.BankTransferRequest;
import com.banking.account_service.client.dto.TransactionRequest;
import com.banking.account_service.config.RabbitMQConfig;
import com.banking.account_service.service.AccountService;
import com.rabbitmq.client.Channel;

import jakarta.transaction.Transactional;

@Service
public class TransactionAndTransferListner {

	@Autowired
	private AccountService accountService;
	
	@RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
	@Transactional
	public void receiveTransactionMessage(@Payload TransactionRequest request, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		try {
			accountService.withdrawOrDepositFunds(request);
		} catch(Exception e)
		{
			System.err.println("Error processing message: " + e.getMessage());
	        try {
	            channel.basicReject(tag, false); // false = do not requeue
	        } catch (IOException ioException) {
	            System.err.println("Error rejecting message: " + ioException.getMessage());
	        }
		}
	}
	
	@RabbitListener(queues = RabbitMQConfig.TRANSFER_QUEUE)
	@Transactional
	public void receiveTransferMessage(@Payload BankTransferRequest request, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		try {
			accountService.transferFunds(request);
		} catch(Exception e)
		{
			System.err.println("Error processing message: " + e.getMessage());
	        try {
	            channel.basicReject(tag, false); // false = do not requeue
	        } catch (IOException ioException) {
	            System.err.println("Error rejecting message: " + ioException.getMessage());
	        }
		}
	}
}
