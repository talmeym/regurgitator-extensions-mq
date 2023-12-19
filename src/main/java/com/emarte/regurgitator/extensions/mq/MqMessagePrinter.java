/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.Log;

import javax.jms.*;
import java.util.Date;
import java.util.Enumeration;

import static com.emarte.regurgitator.core.StringType.stringify;

public class MqMessagePrinter {
    private static final Log log = Log.getLog(MqMessagePrinter.class);
    
    public static void main(String[] args) throws JMSException {
        MqMessagingSystem mqMessagingSystem = MqMessagingLookup.mqMessagingSystem();

        Connection connection = mqMessagingSystem.getConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(mqMessagingSystem.createDestination(args[0]));

        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;

            try {
                log.info("text: " + textMessage.getText());
                log.info("jms-message-id: " + textMessage.getJMSMessageID());
                log.info("type: " + textMessage.getJMSType());
                log.info("destination: " + stringify(textMessage.getJMSDestination()));
                log.info("correlation-id: " + textMessage.getJMSCorrelationID());
                log.info("delivery-mode: " + textMessage.getJMSDeliveryMode());
                long expiration = textMessage.getJMSExpiration();
                log.info("expiration: " + (expiration != 0 ? new Date(expiration).toString() : expiration) + " (now:" + new Date() + ")");
                log.info("priority: " + textMessage.getJMSPriority());
                log.info("redelivered: " + textMessage.getJMSRedelivered());
                log.info("replyTo: " + stringify(textMessage.getJMSReplyTo()));
                log.info("timestamp: " + textMessage.getJMSTimestamp());

                Enumeration<?> enumeration = textMessage.getPropertyNames();

                while(enumeration.hasMoreElements()) {
                    String name = (String) enumeration.nextElement();
                    log.info("property[" + name + "]: " + stringify(textMessage.getObjectProperty(name)));
                }

                log.info("======================");
            } catch (JMSException e) {
                log.error("Error printing jms message", e);
            }
        });

        log.info("Waiting for messages ...");
    }
}
