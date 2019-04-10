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

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.Properties;

import static com.emarte.regurgitator.core.FileUtil.getInputStreamForFile;
import static org.apache.activemq.command.ActiveMQDestination.QUEUE_TYPE;

public class ActiveMqMessagingSystem implements MqMessagingSystem {
    private static final Log log = Log.getLog(ActiveMqMessagingSystem.class);
    private final Properties properties;
    private Connection connection;

    public ActiveMqMessagingSystem() {
        try {
            properties = new Properties();
            properties.load(getInputStreamForFile("classpath:/activemq.properties"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error loading activemq properties", e);
        }
    }

    @Override
    public Connection getConnection() throws JMSException {
        if(connection == null) {
            log.debug("Creating active mq broker connection");
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
            connectionFactory.setProperties(properties);
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
