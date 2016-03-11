package com.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
	public QueueConnection getConnection() throws JMSException;
	public TextMessage createTextMessage();
}
