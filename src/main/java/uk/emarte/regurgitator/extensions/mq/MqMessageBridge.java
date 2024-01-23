/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import uk.emarte.regurgitator.core.Log;
import uk.emarte.regurgitator.core.Regurgitator;
import uk.emarte.regurgitator.core.RegurgitatorException;
import uk.emarte.regurgitator.core.Step;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;
import static uk.emarte.regurgitator.core.ConfigurationFile.loadFile;
import static uk.emarte.regurgitator.extensions.mq.MqMessagingLookup.mqMessagingSystem;

public class MqMessageBridge {
    private static final Log log = Log.getLog(MqMessageBridge.class);

    public MqMessageBridge(String inputDestination, String outputDestination, Regurgitator regurgitator) throws JMSException {
        MqMessagingSystem mqMessagingSystem = mqMessagingSystem();

        log.debug("Creating message consumer");
        Connection connection = mqMessagingSystem.getConnection();
        Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(mqMessagingSystem.createDestination(inputDestination));

        log.debug("Setting up message listener");
        consumer.setMessageListener(new RegurgitatorMessageListener(regurgitator, new MqResponseCallBack(mqMessagingSystem, outputDestination)));

        log.info("Awaiting jms messages");
    }

    public static void main(String[] args) throws RegurgitatorException, JMSException {
        // TODO usage display / input param checking ??

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
