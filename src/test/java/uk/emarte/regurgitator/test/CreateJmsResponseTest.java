/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.test;

import org.junit.Test;
import uk.emarte.regurgitator.core.*;
import uk.emarte.regurgitator.extensions.mq.CreateJmsResponse;
import uk.emarte.regurgitator.extensions.mq.ExtensionsMqConfigConstants;

import java.util.ArrayList;
import java.util.BitSet;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateJmsResponseTest {

    private static final String VALUE = "value";
    private static final String MESSAGE_ID = "messageId";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String DESTINATION = "destination";
    private static final String CORRELATION_ID = "correlationId";
    private static final Long DELIVERY_MODE = 123L;
    private static final Long EXPIRATION = 234L;
    private static final Long PRIORITY = 345L;
    private static final Boolean REDELIVERED = TRUE;
    private static final String REPLY_TO = "replyTo";
    private static final Long TIMESTAMP = System.currentTimeMillis();

    @Test
    public void testThis() throws RegurgitatorException {
        CreateJmsResponse toTest = new CreateJmsResponse(new CreateResponse("id", new ValueSource(null, VALUE), new ArrayList<ValueProcessor>()), MESSAGE_ID, MESSAGE_TYPE, DESTINATION, CORRELATION_ID, DELIVERY_MODE, EXPIRATION, PRIORITY, REDELIVERED, REPLY_TO, TIMESTAMP);
        final BitSet marker = new BitSet(1);

        toTest.execute(new Message(new ResponseCallBack() {
            @Override
            public void respond(Message message, Object value) {
                marker.set(0);
                assertEquals(VALUE, value);
                Parameters responseMetaDataContext = message.getContext(ExtensionsMqConfigConstants.RESPONSE_METADATA_CONTEXT);
                assertEquals(MESSAGE_ID, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_MESSAGE_ID));
                assertEquals(MESSAGE_TYPE, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_TYPE));
                assertEquals(DESTINATION, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_DESTINATION));
                assertEquals(CORRELATION_ID, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_CORRELATION_ID));
                assertEquals(DELIVERY_MODE, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_DELIVERY_MODE));
                assertEquals(EXPIRATION, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_EXPIRATION));
                assertEquals(PRIORITY, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_PRIORITY));
                assertEquals(String.valueOf(REDELIVERED), responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_REDELIVERED));
                assertEquals(REPLY_TO, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_REPLY_TO));
                assertEquals(TIMESTAMP, responseMetaDataContext.getValue(ExtensionsMqConfigConstants.JMS_TIMESTAMP));
            }
        }));

        assertTrue(marker.get(0));
    }
}