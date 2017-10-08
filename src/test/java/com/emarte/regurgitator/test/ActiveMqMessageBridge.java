/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.test;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.extensions.mq.*;

import javax.jms.JMSException;

import static com.emarte.regurgitator.core.ConfigurationFile.loadFile;

public class ActiveMqMessageBridge {
    private static final Log log = Log.getLog(ActiveMqMessageBridge.class);

    public static void main(String[] args) throws RegurgitatorException, JMSException {
        String brokerUri = args[0];
        String inputQueue = args[1];
        String outputQueue = args[2];
        String regurgitatorId = args[3];
        String regurgitatorConfigLocation = args[4];

        log.debug("Loading regurgitator config");
        Step rootStep = loadFile(regurgitatorConfigLocation);

        log.debug("Creating mq message bridge");
        new MqMessageBridge(new ActiveMqMessagingSystem(brokerUri), inputQueue, outputQueue, new Regurgitator(regurgitatorId, rootStep));
        log.debug("Mq message bridge set up");
    }
}
