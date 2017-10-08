/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import javax.jms.*;

public interface MqMessagingSystem {
    Connection getConnection() throws JMSException;
    TextMessage createTextMessage();
    Destination createDestination(String destination);
}
