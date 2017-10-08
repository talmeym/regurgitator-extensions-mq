package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import javax.jms.*;
import javax.jms.Session;

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

        log.debug("Awaiting jms messages");
    }
}
