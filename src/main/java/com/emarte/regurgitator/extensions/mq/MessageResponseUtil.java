package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;

import javax.jms.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

public class MessageResponseUtil {
	private static final Log log = getLog(MessageResponseUtil.class);

	public static void applyResponseData(Message message, TextMessage jmsMessage) throws JMSException {
		addResponseMetadata(message, jmsMessage);
	}

	private static void addResponseMetadata(Message message, TextMessage jmsMessage) throws JMSException {
		log.debug("Adding metadata to jms message from message");
		Parameters context = message.getContext(RESPONSE_METADATA_CONTEXT);

		if(context.contains(JMS_CORRELATION_ID)) {
			Object value = context.getValue(JMS_CORRELATION_ID);
			log.debug("Setting jms correlation id '" + value + "'");
			jmsMessage.setJMSCorrelationID(stringify(value));
		}
	}
}
