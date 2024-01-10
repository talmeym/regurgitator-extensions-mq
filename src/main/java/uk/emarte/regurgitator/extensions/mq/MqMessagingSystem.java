/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

public interface MqMessagingSystem {
    Connection getConnection() throws JMSException;
    TextMessage createTextMessage();
    Destination createDestination(String destination);
}
