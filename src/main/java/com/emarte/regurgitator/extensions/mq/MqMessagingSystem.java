package com.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
	public Connection getConnection() throws JMSException;
	public TextMessage createTextMessage();
	public Destination createDestination(String destination);
}
