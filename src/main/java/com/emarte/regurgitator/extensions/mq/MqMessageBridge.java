package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import javax.jms.*;
import javax.jms.Session;

public class MqMessageBridge {
	private static Log log = Log.getLog(MqMessageBridge.class);

	public MqMessageBridge(MqMessagingSystem mqMessagingSystem, String inputDestination, String outputDestination, Regurgitator regurgitator) throws JMSException, RegurgitatorException {
		log.debug("Creating message consumer");
		Connection connection = mqMessagingSystem.getConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(mqMessagingSystem.createDestination(inputDestination));
		log.debug("Setting up message listener");
		consumer.setMessageListener(new RegurgitatorMessageListener(regurgitator, new MqResponseCallback(mqMessagingSystem, outputDestination)));
		log.debug("Awaiting jms messages");
	}
}
