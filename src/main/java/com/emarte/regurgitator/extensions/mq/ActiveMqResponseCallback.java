package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.MessageResponseUtil.applyResponseData;

public class ActiveMqResponseCallback implements ResponseCallBack {
	private static final Log log = getLog(ActiveMqResponseCallback.class);
	private QueueConnection connection;
	private String outputQueue;

	public ActiveMqResponseCallback(QueueConnection connection, String outputQueue) {
		this.connection = connection;
		this.outputQueue = outputQueue;
	}

	@Override
	public void respond(Message message, Object value) {
		try {
			log.debug("Processing callback");
			QueueSession session = connection.createQueueSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			QueueSender sender = session.createSender(session.createQueue(outputQueue));
			javax.jms.TextMessage jmsMessage = new ActiveMQTextMessage();

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
