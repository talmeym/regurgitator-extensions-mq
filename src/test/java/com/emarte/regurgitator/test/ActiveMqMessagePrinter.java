/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.test;

import javax.jms.*;
import java.util.*;

import static com.emarte.regurgitator.core.StringType.stringify;

public class ActiveMqMessagePrinter {
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
                    System.out.println("\ntext: " + textMessage.getText());
                    System.out.println("jms-message-id: " + textMessage.getJMSMessageID());
                    System.out.println("type: " + textMessage.getJMSType());
                    System.out.println("destination: " + stringify(textMessage.getJMSDestination()));
                    System.out.println("correlation-id: " + textMessage.getJMSCorrelationID());
                    System.out.println("delivery-mode: " + textMessage.getJMSDeliveryMode());
                    long expiration = textMessage.getJMSExpiration();
                    System.out.println("expiration: " + (expiration != 0 ? new Date(expiration).toString() : expiration) + " (now:" + new Date() + ")");
                    System.out.println("priority: " + textMessage.getJMSPriority());
                    System.out.println("redelivered: " + textMessage.getJMSRedelivered());
                    System.out.println("replyTo: " + stringify(textMessage.getJMSReplyTo()));
                    System.out.println("timestamp: " + textMessage.getJMSTimestamp());

                    Enumeration enumeration = textMessage.getPropertyNames();

                    while(enumeration.hasMoreElements()) {
                        String name = (String) enumeration.nextElement();
                        System.out.println("property[" + name + "]: " + stringify(textMessage.getObjectProperty(name)));
                    }

                    System.out.println("\n======================");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Waiting for messages ...");
    }
}
