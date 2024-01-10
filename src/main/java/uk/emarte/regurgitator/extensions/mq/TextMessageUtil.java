/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import uk.emarte.regurgitator.core.Log;
import uk.emarte.regurgitator.core.Message;
import uk.emarte.regurgitator.core.Parameters;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.StringType.stringify;
import static uk.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

class TextMessageUtil {
    private static final Log log = getLog(TextMessageUtil.class);

    static void applyResponseData(Message message, TextMessage jmsMessage, MqMessagingSystem mqMessagingSystem) throws JMSException {
        addMetadata(message.getContext(RESPONSE_METADATA_CONTEXT), jmsMessage, mqMessagingSystem);
        addProperties(message.getContext(RESPONSE_PROPERTIES_CONTEXT), jmsMessage);
    }

    static void applyRequestData(Message message, TextMessage jmsMessage, MqMessagingSystem mqMessagingSystem) throws JMSException {
        addMetadata(message.getContext(REQUEST_METADATA_CONTEXT), jmsMessage, mqMessagingSystem);
        addProperties(message.getContext(REQUEST_PROPERTIES_CONTEXT), jmsMessage);
    }

    private static void addMetadata(Parameters context, TextMessage jmsMessage, MqMessagingSystem mqMessagingSystem) throws JMSException {
        log.debug("Adding metadata to jms message from message");

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
            jmsMessage.setJMSDeliveryMode(longify(value).intValue());
        }

        if(context.contains(JMS_EXPIRATION)) {
            Object value = context.getValue(JMS_EXPIRATION);
            log.debug("Setting jms expiration '" + value + "'");
            jmsMessage.setJMSExpiration(longify(value));
        }

        if(context.contains(JMS_PRIORITY)) {
            Object value = context.getValue(JMS_PRIORITY);
            log.debug("Setting jms priority '" + value + "'");
            jmsMessage.setJMSPriority(longify(value).intValue());
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

    private static Boolean boolify(Object value) {
        return Boolean.parseBoolean(stringify(value));
    }

    private static Long longify(Object value) {
        return Long.parseLong(stringify(value));
    }

    private static void addProperties(Parameters context, TextMessage jmsMessage) throws JMSException {
        log.debug("Adding properties to jms message from message");

        for(Object id : context.ids()) {
            Object value = context.getValue(id);
            log.debug("Setting property '" + id + "' to value '" + value + "'");
            jmsMessage.setObjectProperty(stringify(id), value);
        }
    }
}
