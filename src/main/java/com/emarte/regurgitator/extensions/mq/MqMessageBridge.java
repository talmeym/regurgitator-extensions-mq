package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import javax.jms.*;
import javax.jms.Session;

public class MqMessageBridge {
	public MqMessageBridge(MqMessagingSystem mqMessagingSystem, String inputQueue, String outputQueue, Regurgitator regurgitator) throws JMSException, RegurgitatorException {
		QueueConnection connection = mqMessagingSystem.getConnection();
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueReceiver receiver = session.createReceiver(session.createQueue(inputQueue));
		receiver.setMessageListener(new RegurgitatorMessageListener(regurgitator, new MqResponseCallback(mqMessagingSystem, outputQueue)));
	}
}
