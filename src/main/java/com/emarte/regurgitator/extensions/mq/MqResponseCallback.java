package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;

import javax.jms.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.MessageResponseUtil.applyResponseData;

public class MqResponseCallback implements ResponseCallBack {
	private static final Log log = getLog(MqResponseCallback.class);
	private final MqMessagingSystem mqMessagingSystem;
	private final String outputQueue;

	public MqResponseCallback(MqMessagingSystem mqMessagingSystem, String outputQueue) {
		this.mqMessagingSystem = mqMessagingSystem;
		this.outputQueue = outputQueue;
	}

	@Override
	public void respond(Message message, Object value) {
		try {
			log.debug("Processing callback");
			QueueSession session = mqMessagingSystem.getConnection().createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(session.createQueue(outputQueue));
			javax.jms.TextMessage jmsMessage = mqMessagingSystem.createTextMessage();

			log.debug("Applying message data to jms message");
			applyResponseData(message, jmsMessage);

			log.debug("Adding response payload to jms message");
			jmsMessage.setText(stringify(value));

			log.debug("Sending response message");
			sender.send(jmsMessage);
			session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
