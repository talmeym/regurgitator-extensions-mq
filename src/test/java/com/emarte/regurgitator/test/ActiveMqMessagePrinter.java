package com.emarte.regurgitator.test;

import javax.jms.*;
import java.util.*;

public class ActiveMqMessagePrinter {
	public static void main(String[] args) throws JMSException {
		String brokerUri = args[0];
		String inputQueue = args[1];

		Connection connection = new ActiveMqMessagingSystem(brokerUri).getConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(inputQueue));

		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;

				try {
					String text = textMessage.getText();
					System.out.println("\ntext: " + text);

					String messageID = textMessage.getJMSMessageID();
					System.out.println("jms-message-id: " + messageID);

					String type = textMessage.getJMSType();
					System.out.println("type: " + type);

					String destination = String.valueOf(textMessage.getJMSDestination());
					System.out.println("destination: " + destination);

					String correlationID = textMessage.getJMSCorrelationID();
					System.out.println("correlation-id: " + correlationID);

					int deliveryMode = textMessage.getJMSDeliveryMode();
					System.out.println("delivery-mode: " + deliveryMode);

					long expiration = textMessage.getJMSExpiration();
					System.out.println("expiration: " + (expiration != 0 ? new Date(expiration).toString() : expiration) + " (now:" + new Date() + ")");

					int priority = textMessage.getJMSPriority();
					System.out.println("priority: " + priority);

					boolean redelivered = textMessage.getJMSRedelivered();
					System.out.println("redelivered: " + redelivered);

					String replyTo = String.valueOf(textMessage.getJMSReplyTo());
					System.out.println("replyTo: " + replyTo);

					long timestamp = textMessage.getJMSTimestamp();
					System.out.println("timestamp: " + timestamp);

					Enumeration enumeration = textMessage.getPropertyNames();

					while(enumeration.hasMoreElements()) {
						String name = (String) enumeration.nextElement();
						Object value = textMessage.getObjectProperty(name);
						System.out.println("property[" + name + "]: " + String.valueOf(value));
					}

					System.out.println("\n======================");
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("Waiting for messages ...");
	}
}
