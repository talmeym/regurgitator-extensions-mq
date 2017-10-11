/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.test;

import com.emarte.regurgitator.core.Log;
import com.emarte.regurgitator.extensions.mq.MqMessagingSystem;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

import static org.apache.activemq.command.ActiveMQDestination.QUEUE_TYPE;

public class ActiveMqMessagingSystem implements MqMessagingSystem {
    private static final Log log = Log.getLog(ActiveMqMessagingSystem.class);

    private final String brokerUri;
    private Connection connection;

    public ActiveMqMessagingSystem(String brokerUri) {
        this.brokerUri = brokerUri;
    }

    @Override
    public Connection getConnection() throws JMSException {
        if(connection == null) {
            log.debug("Creating active mq broker connection");
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setBrokerURL(brokerUri);
            connection = connectionFactory.createConnection();
            connection.start();
            log.debug("Broker connection started");
        }

        return connection;
    }

    @Override
    public TextMessage createTextMessage() {
        log.debug("Creating text message");
        return new ActiveMQTextMessage();
    }

    @Override
    public Destination createDestination(String destination) {
        log.debug("Creating destination '{}'", destination);
        return ActiveMQDestination.createDestination(destination, /* default */ QUEUE_TYPE);
    }
}
