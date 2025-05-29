package com.banking.transaction_service.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	
	public static final String TRANSACTION_QUEUE = "transaction.account.queue";
	public static final String TRANSFER_QUEUE = "transfer.account.queue";
	public static final String STATUS_QUEUE = "account.status.queue";
	
	@Bean
	public Queue statusQueue() {
		return new Queue(STATUS_QUEUE,true);
	}
	
	@Bean
	public Queue transactionQueue() {
		return new Queue(TRANSACTION_QUEUE,true);
	}
	
	@Bean
	public Queue transferQueue() {
		return new Queue(TRANSFER_QUEUE,true);
	}
	
	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // Ensures JSON serialization
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
	
}
