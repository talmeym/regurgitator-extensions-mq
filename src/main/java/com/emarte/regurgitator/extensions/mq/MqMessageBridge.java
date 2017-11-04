/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.Log;
import com.emarte.regurgitator.core.Regurgitator;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class MqMessageBridge {
    private static final Log log = Log.getLog(MqMessageBridge.class);

    public MqMessageBridge(MqMessagingSystem mqMessagingSystem, String inputDestination, String outputDestination, Regurgitator regurgitator) throws JMSException {
        log.debug("Creating message consumer");
        Connection connection = mqMessagingSystem.getConnection();
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(mqMessagingSystem.createDestination(inputDestination));

        log.debug("Setting up message listener");
        consumer.setMessageListener(new RegurgitatorMessageListener(regurgitator, new MqResponseCallback(mqMessagingSystem, outputDestination)));

        log.info("Awaiting jms messages");
    }
}
