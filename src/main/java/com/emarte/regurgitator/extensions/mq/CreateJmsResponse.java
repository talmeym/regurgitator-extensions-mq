package com.emarte.regurgitator.extensions.mq;

import com.emarte.regurgitator.core.*;

import static com.emarte.regurgitator.core.Log.getLog;
import static com.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants.*;

public class CreateJmsResponse extends Identifiable implements Step {
    private final Log log = getLog(this);
    private final CreateResponse response;
    private final String messageId;
    private final String type;
    private final String destination;
    private final String correlationId;
    private final String deliveryMode;
    private final String expiration;
    private final String priority;
    private final String redelivered;
    private final String replyTo;
    private final String timestamp;

    public CreateJmsResponse(CreateResponse response, String messageId, String type, String destination, String correlationId, String deliveryMode, String expiration, String priority, String redelivered, String replyTo, String timestamp) {
        super(response.getId());
        this.response = response;
        this.messageId = messageId;
        this.destination = destination;
        this.correlationId = correlationId;
        this.type = type;
        this.deliveryMode = deliveryMode;
        this.expiration = expiration;
        this.priority = priority;
        this.redelivered = redelivered;
        this.replyTo = replyTo;
        this.timestamp = timestamp;
    }

    @Override
    public void execute(Message message) throws RegurgitatorException {
        Parameters responseMetadata = message.getContext(RESPONSE_METADATA_CONTEXT);

        if(messageId != null) {
            responseMetadata.setValue(JMS_MESSAGE_ID, messageId);
        }

        if(type != null) {
            responseMetadata.setValue(JMS_TYPE, type);
        }

        if(destination != null) {
            responseMetadata.setValue(JMS_DESTINATION, destination);
        }

        if(correlationId != null) {
            responseMetadata.setValue(JMS_CORRELATION_ID, correlationId);
        }

        if(deliveryMode != null) {
            responseMetadata.setValue(JMS_DELIVERY_MODE, deliveryMode);
        }

        if(expiration != null) {
            responseMetadata.setValue(JMS_EXPIRATION, expiration);
        }

        if(priority != null) {
            responseMetadata.setValue(JMS_PRIORITY, priority);
        }

        if(redelivered != null) {
            responseMetadata.setValue(JMS_REDELIVERED, redelivered);
        }

        if(replyTo != null) {
            responseMetadata.setValue(JMS_REPLY_TO, replyTo);
        }

        if(timestamp != null) {
            responseMetadata.setValue(JMS_TIMESTAMP, timestamp);
        }

        response.execute(message, log);
    }
}
