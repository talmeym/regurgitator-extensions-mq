package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;
import com.emarte.regurgitator.core.Message;

import javax.jms.*;
import javax.jms.Session;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.core.StringType.stringify;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.JMS_DESTINATION;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.RESPONSE_METADATA_CONTEXT;
import static com.emarte.regurgitator.extensions.mq.MessageResponseUtil.applyResponseData;

class MqResponseCallback implements ResponseCallBack {
    private static final Log log = getLog(MqResponseCallback.class);
    private final MqMessagingSystem mqMessagingSystem;
    private final String defaultOutputDestination;

    MqResponseCallback(MqMessagingSystem mqMessagingSystem, String defaultOutputDestination) {
        this.mqMessagingSystem = mqMessagingSystem;
        this.defaultOutputDestination = defaultOutputDestination;
    }

    @Override
    public void respond(Message message, Object value) {
        try {
            log.debug("Processing callback");
            Parameter destinationParameter = message.getContextValue(new ContextLocation(RESPONSE_METADATA_CONTEXT, JMS_DESTINATION));
            String destination = destinationParameter != null ? stringify(destinationParameter.getValue()) : defaultOutputDestination;

            if(destinationParameter != null) {
                log.debug("Overriding jms destination with parameter value '" + destination + "'");
            }

            Session session = mqMessagingSystem.getConnection().createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(mqMessagingSystem.createDestination(destination));

            TextMessage jmsMessage = mqMessagingSystem.createTextMessage();

            log.debug("Applying message data to jms message");
            applyResponseData(message, jmsMessage, mqMessagingSystem);

            log.debug("Adding response payload to jms message");
            jmsMessage.setText(stringify(value));

            log.debug("Sending response message");
            producer.send(jmsMessage);
            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
