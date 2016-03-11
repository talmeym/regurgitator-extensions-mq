package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.Session;

import static com.emarte.regurgitator.core.ConfigurationFile.loadFile;

public class ActiveMqMessageBridge {
	public ActiveMqMessageBridge(String brokerUri, String inputQueue, String outputQueue, String regurgitatorId, String configLocation) throws JMSException, RegurgitatorException {
		Regurgitator regurgitator = new Regurgitator(regurgitatorId, loadFile(configLocation));

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUri);
		QueueConnection connection = connectionFactory.createQueueConnection();
		connection.start();

		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(inputQueue);
		QueueReceiver receiver = session.createReceiver(queue);

		receiver.setMessageListener(new RegurgitatorMessageListener(regurgitator, new ActiveMqResponseCallback(connection, outputQueue)));
	}

	public static void main(String[] args) {
		try {
			new ActiveMqMessageBridge(args[0], args[1], args[2], args[3], args[4]);
		} catch (Exception e) {
			System.err.println("Error setting up message bridge");
			e.printStackTrace();
		}
	}
}
