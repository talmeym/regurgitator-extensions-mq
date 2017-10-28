/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.test;

import com.emarte.regurgitator.core.Log;

import javax.jms.*;
import java.util.Date;
import java.util.Enumeration;

import static com.emarte.regurgitator.core.StringType.stringify;

public class ActiveMqMessagePrinter {
    private static final Log log = Log.getLog(ActiveMqMessagePrinter.class);
    
    public static void main(String[] args) throws JMSException {
        String brokerUri = args[0];
        String inputQueue = args[1];

        Connection connection = new ActiveMqMessagingSystem(brokerUri).getConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(session.createQueue(inputQueue));

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;

                try {
                    log.info("\ntext: " + textMessage.getText());
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

                    Enumeration enumeration = textMessage.getPropertyNames();

                    while(enumeration.hasMoreElements()) {
                        String name = (String) enumeration.nextElement();
                        log.info("property[" + name + "]: " + stringify(textMessage.getObjectProperty(name)));
                    }

                    log.info("\n======================");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        log.info("Waiting for messages ...");
    }
}
