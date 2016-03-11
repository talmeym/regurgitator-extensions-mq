package com.emarte.regurgitator.extensions.mq;

public interface ExtensionsMqConfigConstants {
	// contexts
    public static final String REQUEST_METADATA_CONTEXT = "request-metadata";
	public static final String REQUEST_PROPERTIES_CONTEXT = "request-properties";
    public static final String REQUEST_PAYLOAD_CONTEXT = "request-payload";
	public static final String RESPONSE_METADATA_CONTEXT = "response-metadata";

	// metadata
	public static final String JMS_MESSAGE_ID = "jms-message-id";
	public static final String JMS_TYPE = "jms-type";
	public static final String JMS_CORRELATION_ID = "jms-correlation-id";
	public static final String JMS_DESTINATION = "jms-destination";
	public static final String JMS_DELIVERY_MODEL = "jms-delivery-mode";
	public static final String JMS_EXPIRATION = "jms-expiration";
	public static final String JMS_PRIORITY = "jms-priority";
	public static final String JMS_REDELiVERED = "jms-redelivered";
	public static final String JMS_REPLY_TO = "jms-reply-to";
	public static final String JMS_TIMESTAMP = "jms-timestamp";
	public static final String TEXT = "text";
}
