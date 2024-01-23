/*
 * Copyright (C) 2017 Miles Talmey.
 * Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */
package uk.emarte.regurgitator.extensions.mq;

import uk.emarte.regurgitator.core.Log;
import uk.emarte.regurgitator.core.Message;
import uk.emarte.regurgitator.core.Regurgitator;
import uk.emarte.regurgitator.core.ResponseCallBack;

import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static uk.emarte.regurgitator.core.Log.getLog;
import static uk.emarte.regurgitator.extensions.mq.MessageUtil.applyRequestData;

public class RegurgitatorMessageListener implements MessageListener {
    private static final Log log = getLog(RegurgitatorMessageListener.class);

    private final Regurgitator regurgitator;
    private final ResponseCallBack responseCallBack;

    public RegurgitatorMessageListener(Regurgitator regurgitator, ResponseCallBack responseCallBack) {
        this.regurgitator = regurgitator;
        this.responseCallBack = responseCallBack;
    }

    @Override
    public void onMessage(javax.jms.Message message) {
        log.debug("Accepting new jms message");
        TextMessage textMessage = (TextMessage) message;

        try {
            log.debug("Creating new message");
            Message newMessage = new Message(responseCallBack);

            log.debug("Applying jms message data to message");
            applyRequestData(textMessage, newMessage);

            log.debug("Sending message to regurgitator");
            regurgitator.processMessage(newMessage);
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO error handling ??
        }
    }
}
