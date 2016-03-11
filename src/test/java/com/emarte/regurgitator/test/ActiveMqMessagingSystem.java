package com.emarte.regurgitator.test;

import com.emarte.regurgitator.extensions.mq.MqMessagingSystem;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

public class ActiveMqMessagingSystem implements MqMessagingSystem {
	private final String brokerUri;
	private QueueConnection connection;

	public ActiveMqMessagingSystem(String brokerUri) throws JMSException {
		this.brokerUri = brokerUri;
	}

	@Override
	public QueueConnection getConnection() throws JMSException {
		if(connection == null) {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			connectionFactory.setBrokerURL(brokerUri);
			connection = connectionFactory.createQueueConnection();
			connection.start();
		}

		return connection;
	}

	@Override
	public TextMessage createTextMessage() {
		return new ActiveMQTextMessage();
	}
}
