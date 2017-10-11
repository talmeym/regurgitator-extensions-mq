/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

class MessageResponseUtil {
    private static final Log log = getLog(MessageResponseUtil.class);

    static void applyResponseData(Message message, TextMessage jmsMessage, MqMessagingSystem mqMessagingSystem) throws JMSException {
        addResponseMetadata(message, jmsMessage, mqMessagingSystem);
        addResponseProperties(message, jmsMessage);
    }

    private static void addResponseMetadata(Message message, TextMessage jmsMessage, MqMessagingSystem mqMessagingSystem) throws JMSException {
        log.debug("Adding metadata to jms message from message");
        Parameters context = message.getContext(RESPONSE_METADATA_CONTEXT);

        if(context.contains(JMS_MESSAGE_ID)) {
            Object value = context.getValue(JMS_MESSAGE_ID);
            log.debug("Setting jms message id '" + value + "'");
            jmsMessage.setJMSMessageID(stringify(value));
        }

        if(context.contains(JMS_TYPE)) {
            Object value = context.getValue(JMS_TYPE);
            log.debug("Setting jms type '" + value + "'");
            jmsMessage.setJMSType(stringify(value));
        }

        if(context.contains(JMS_DESTINATION)) {
            Object value = context.getValue(JMS_DESTINATION);
            log.debug("Setting jms destination '" + value + "'");
            jmsMessage.setJMSDestination(mqMessagingSystem.createDestination(stringify(value)));
        }

        if(context.contains(JMS_CORRELATION_ID)) {
            Object value = context.getValue(JMS_CORRELATION_ID);
            log.debug("Setting jms correlation id '" + value + "'");
            jmsMessage.setJMSCorrelationID(stringify(value));
        }

        if(context.contains(JMS_DELIVERY_MODE)) {
            Object value = context.getValue(JMS_DELIVERY_MODE);
            log.debug("Setting jms delivery mode '" + value + "'");
            jmsMessage.setJMSDeliveryMode(intify(value));
        }

        if(context.contains(JMS_EXPIRATION)) {
            Object value = context.getValue(JMS_EXPIRATION);
            log.debug("Setting jms expiration '" + value + "'");
            jmsMessage.setJMSExpiration(intify(value));
        }

        if(context.contains(JMS_PRIORITY)) {
            Object value = context.getValue(JMS_PRIORITY);
            log.debug("Setting jms priority '" + value + "'");
            jmsMessage.setJMSPriority(intify(value));
        }

        if(context.contains(JMS_REDELIVERED)) {
            Object value = context.getValue(JMS_REDELIVERED);
            log.debug("Setting jms redelivered '" + value + "'");
            jmsMessage.setJMSRedelivered(boolify(value));
        }

        if(context.contains(JMS_REPLY_TO)) {
            Object value = context.getValue(JMS_REPLY_TO);
            log.debug("Setting jms reply to '" + value + "'");
            jmsMessage.setJMSReplyTo(mqMessagingSystem.createDestination(stringify(value)));
        }

        if(context.contains(JMS_TIMESTAMP)) {
            Object value = context.getValue(JMS_TIMESTAMP);
            log.debug("Setting jms timestamp '" + value + "'");
            jmsMessage.setJMSTimestamp(longify(value));
        }
    }

    private static boolean boolify(Object value) {
        return Boolean.parseBoolean(stringify(value));
    }

    private static int intify(Object value) {
        return Integer.parseInt(stringify(value));
    }

    private static long longify(Object value) {
        return Long.parseLong(stringify(value));
    }

    private static void addResponseProperties(Message message, TextMessage jmsMessage) throws JMSException {
        log.debug("Adding properties to jms message from message");
        Parameters context = message.getContext(RESPONSE_PROPERTIES_CONTEXT);

        for(Object id : context.ids()) {
            Object value = context.getValue(id);
            log.debug("Setting property '" + id + "' to value '" + value + "'");
            jmsMessage.setObjectProperty(stringify(id), value);
        }
    }
}
