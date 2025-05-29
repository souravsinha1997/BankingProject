package com.banking.transaction_service.rabbitMQ;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.banking.transaction_service.client.dto.Status;
import com.banking.transaction_service.config.RabbitMQConfig;
import com.banking.transaction_service.service.TransactionService;
import com.rabbitmq.client.Channel;

import jakarta.transaction.Transactional;

@Service
public class StatusListener {

	@Autowired
	private TransactionService transactionService;
	
	@RabbitListener(queues = RabbitMQConfig.STATUS_QUEUE)
	@Transactional
	public void receiveTransactionMessage(@Payload Status status, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
		try {
			transactionService.updateTransaction(status);
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
