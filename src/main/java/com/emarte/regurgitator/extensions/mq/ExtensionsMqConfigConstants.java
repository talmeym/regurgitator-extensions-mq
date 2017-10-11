/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package com.emarte.regurgitator.extensions.mq;

public interface ExtensionsMqConfigConstants {
    String REQUEST_METADATA_CONTEXT = "request-metadata";
    String REQUEST_PROPERTIES_CONTEXT = "request-properties";
    String REQUEST_PAYLOAD_CONTEXT = "request-payload";
    String RESPONSE_METADATA_CONTEXT = "response-metadata";
    String RESPONSE_PROPERTIES_CONTEXT = "response-properties";
    String JMS_MESSAGE_ID = "jms-message-id";
    String JMS_TYPE = "jms-type";
    String JMS_DESTINATION = "jms-destination";
    String JMS_CORRELATION_ID = "jms-correlation-id";
    String JMS_DELIVERY_MODE = "jms-delivery-mode";
    String JMS_EXPIRATION = "jms-expiration";
    String JMS_PRIORITY = "jms-priority";
    String JMS_REDELIVERED = "jms-redelivered";
    String JMS_REPLY_TO = "jms-reply-to";
    String JMS_TIMESTAMP = "jms-timestamp";
    String TEXT = "text";
}
