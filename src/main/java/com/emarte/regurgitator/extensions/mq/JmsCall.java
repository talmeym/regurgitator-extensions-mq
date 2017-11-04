/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;

import javax.jms.*;
import javax.jms.Session;

import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.REQUEST_PAYLOAD_CONTEXT;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.TEXT;
import static com.emarte.regurgitator.extensions.mq.TextMessageUtil.applyRequestData;
import static javax.jms.Session.AUTO_ACKNOWLEDGE;

public class JmsCall extends Identifiable implements Step {
    private final Log log = Log.getLog(this);
    private final String destination;
    private final MqMessagingSystem mqMessagingSystem;

    public JmsCall(Object id, MqMessagingSystem mqMessagingSystem, String destination) {
        super(id);
        this.destination = destination;
        this.mqMessagingSystem = mqMessagingSystem;
    }

    @Override
    public void execute(Message message) throws RegurgitatorException {
        try {
            log.debug("Creating text message");
            TextMessage jmsMessage = mqMessagingSystem.createTextMessage();

            log.debug("Applying message data to jms message");
            applyRequestData(message, jmsMessage, mqMessagingSystem);
            addPayload(message, jmsMessage);

            log.debug("Sending jms message");
            Connection connection = mqMessagingSystem.getConnection();
            Session session = connection.createSession(false, AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(mqMessagingSystem.createDestination(destination));
            producer.send(jmsMessage);
        } catch (JMSException e) {
            throw new RegurgitatorException("Error sending jms message", e);
        }
    }

    private void addPayload(Message message, TextMessage jmsMessage) throws JMSException {
        Object payload = message.getContext(REQUEST_PAYLOAD_CONTEXT).getValue(TEXT);

        if(payload != null) {
            log.debug("Adding payload to jms message: '{}'", payload);
            jmsMessage.setText(stringify(payload));
        }
    }
}