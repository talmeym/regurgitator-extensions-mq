package com.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
    Connection getConnection() throws JMSException;
    TextMessage createTextMessage();
    Destination createDestination(String destination);
}
