/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.Log;
import com.emarte.regurgitator.core.Regurgitator;
import com.emarte.regurgitator.core.RegurgitatorException;
import com.emarte.regurgitator.core.Step;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import static com.emarte.regurgitator.core.ConfigurationFile.loadFile;
import static com.emarte.regurgitator.extensions.mq.MqMessagingLookup.mqMessagingSystem;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class MqMessageBridge {
    private static final Log log = Log.getLog(MqMessageBridge.class);

    public MqMessageBridge(String inputDestination, String outputDestination, Regurgitator regurgitator) throws JMSException {
        MqMessagingSystem mqMessagingSystem = mqMessagingSystem();

        log.debug("Creating message consumer");
        Connection connection = mqMessagingSystem.getConnection();
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(mqMessagingSystem.createDestination(inputDestination));

        log.debug("Setting up message listener");
        consumer.setMessageListener(new RegurgitatorMessageListener(regurgitator, new MqResponseCallback(mqMessagingSystem, outputDestination)));

        log.info("Awaiting jms messages");
    }

    public static void main(String[] args) throws RegurgitatorException, JMSException {
        String inputQueue = args[0];
        String outputQueue = args[1];
        String regurgitatorId = args[2];
        String regurgitatorConfigLocation = args[3];

        log.debug("Loading regurgitator config");
        Step rootStep = loadFile(regurgitatorConfigLocation);

        log.debug("Creating mq message bridge");
        new MqMessageBridge(inputQueue, outputQueue, new Regurgitator(regurgitatorId, rootStep));
        log.debug("Mq message bridge set up");
    }
}
