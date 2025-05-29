package com.banking.account_service.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.account_service.client.dto.Status;
import com.banking.account_service.config.RabbitMQConfig;


@Service
public class StatusPublisher {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	
	public void sendTransactionQueue(Status status) {
		amqpTemplate.convertAndSend(RabbitMQConfig.STATUS_QUEUE,status);
		System.out.println("TXN status for reference no : "+status.getTxnRefNo()+" - "+status.getStatus());
	}
}
