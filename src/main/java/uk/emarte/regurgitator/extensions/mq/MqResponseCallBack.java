/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import uk.emarte.regurgitator.core.*;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.core.StringType.stringify;
import static uk.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.JMS_DESTINATION;
import static uk.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.RESPONSE_METADATA_CONTEXT;
import static uk.emarte.regurgitator.extensions.mq.TextMessageUtil.applyResponseData;

class MqResponseCallBack implements ResponseCallBack {
    private static final Log log = getLog(MqResponseCallBack.class);
    private final MqMessagingSystem mqMessagingSystem;
    private final String defaultOutputDestination;

    MqResponseCallBack(MqMessagingSystem mqMessagingSystem, String defaultOutputDestination) {
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
                log.debug("Overriding default jms destination '{}' with parameter value '{}'", defaultOutputDestination, destination);
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
            e.printStackTrace(); // TODO error handling ??
        }
    }
}
