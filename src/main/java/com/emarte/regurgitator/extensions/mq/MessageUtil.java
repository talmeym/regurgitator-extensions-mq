/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.Log;
import com.emarte.regurgitator.core.Message;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Enumeration;

import static com.emarte.regurgitator.core.CoreTypes.NUMBER;
import static com.emarte.regurgitator.core.CoreTypes.STRING;
import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

class MessageUtil {
    private static final Log log = getLog(MessageUtil.class);

    static void applyRequestData(TextMessage jmsMessage, Message message) throws JMSException {
        addMetadata(jmsMessage, message);
        addProperties(jmsMessage, message);
        addPayload(jmsMessage, message);
    }

    private static void addMetadata(TextMessage jmsMessage, Message message) throws JMSException {
        log.debug("Adding metadata to message from jms message");
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_MESSAGE_ID, jmsMessage.getJMSMessageID());
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_TYPE, jmsMessage.getJMSType());
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_DESTINATION, stringify(jmsMessage.getJMSDestination()));
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_CORRELATION_ID, jmsMessage.getJMSCorrelationID());
        addIntegerParam(message, REQUEST_METADATA_CONTEXT, JMS_DELIVERY_MODE, jmsMessage.getJMSDeliveryMode());
        addLongParam(message, REQUEST_METADATA_CONTEXT, JMS_EXPIRATION, jmsMessage.getJMSExpiration());
        addIntegerParam(message, REQUEST_METADATA_CONTEXT, JMS_PRIORITY, jmsMessage.getJMSPriority());
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_REDELIVERED, stringify(jmsMessage.getJMSRedelivered()));
        addStringParam(message, REQUEST_METADATA_CONTEXT, JMS_REPLY_TO, stringify(jmsMessage.getJMSReplyTo()));
        addLongParam(message, REQUEST_METADATA_CONTEXT, JMS_TIMESTAMP, jmsMessage.getJMSTimestamp());
    }

    @SuppressWarnings("unchecked")
    private static void addProperties(TextMessage jmsMessage, Message message) throws JMSException {
        log.debug("Adding properties to message from jms message");
        Enumeration<String> propertyNames = jmsMessage.getPropertyNames();

        while(propertyNames.hasMoreElements()) {
            String name = propertyNames.nextElement();
            Object value = jmsMessage.getObjectProperty(name);

            if(value instanceof Integer) {
                addIntegerParam(message, REQUEST_PROPERTIES_CONTEXT, name, (Integer) value);
            } else {
                addStringParam(message, REQUEST_PROPERTIES_CONTEXT, name, stringify(value));
            }
        }
    }

    private static void addPayload(TextMessage jmsMessage, Message message) throws JMSException {
        log.debug("Adding payload to message from jms message");
        addStringParam(message, REQUEST_PAYLOAD_CONTEXT, TEXT, jmsMessage.getText());
    }

    private static void addStringParam(Message message, String context, String name, String value) {
        if(value != null && value.length() > 0) {
            message.getContext(context).setValue(name, STRING, value);
        }
    }

    private static void addIntegerParam(Message message, String context, String name, Integer value) {
        if(value != null) {
            message.getContext(context).setValue(name, NUMBER, (long) value);
        }
    }
    private static void addLongParam(Message message, String context, String name, Long value) {
        if(value != null) {
            message.getContext(context).setValue(name, NUMBER, value);
        }
    }
}
